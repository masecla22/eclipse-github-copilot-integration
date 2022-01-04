/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonSyntaxException
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.util.Pair
 *  com.intellij.openapi.util.text.StringUtil
 *  it.unimi.dsi.fastutil.doubles.DoubleArrayList
 *  it.unimi.dsi.fastutil.doubles.DoubleList
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMaps
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 *  it.unimi.dsi.fastutil.ints.IntArrayList
 *  it.unimi.dsi.fastutil.ints.IntList
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.openai;

import com.github.copilot.openai.APIChoice;
import com.github.copilot.openai.APIJsonDataStreaming;
import com.github.copilot.openai.APILogprobs;
import com.github.copilot.openai.CompletionResponseInfo;
import com.github.copilot.openai.DefaultAPIChoice;
import com.github.copilot.openai.OpenAI;
import com.github.copilot.openai.OpenAICompletionResponse;
import com.github.copilot.openai.OpenAIHttpUtil;
import com.github.copilot.openai.StringDoublePair;
import com.github.copilot.request.EditorRequestUtil;
import com.github.copilot.request.LanguageEditorRequest;
import com.github.copilot.telemetry.TelemetryData;
import com.github.copilot.telemetry.TelemetryService;
import com.github.copilot.util.CopilotStringUtil;
import com.github.copilot.util.Maps;
import com.github.copilot.util.String2DoubleMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;

class APIChoiceTransformer {
	private static final Logger LOG = Logger.getInstance(APIChoiceTransformer.class);
	private static final Pattern NEWLINE_PATTERN = Pattern.compile("\n");
	private final LanguageEditorRequest request;
	private final TelemetryData telemetryBaseData;
	private final Consumer<APIChoice> onNewItem;
	private final Int2ObjectMap<APIJsonDataStreaming> solutions;
	private volatile String completionId;
	private volatile int completionCreatedTimestamp;
	private volatile CompletionResponseInfo completionResponseInfo;

	APIChoiceTransformer(LanguageEditorRequest request, TelemetryData telemetryBaseData,
			Consumer<APIChoice> onNewItem) {
		if (request == null) {
			throw new IllegalStateException("request cannot be null!");
		}
		if (telemetryBaseData == null) {
			throw new IllegalStateException("telemetryBaseData cannot be null!");
		}
		if (onNewItem == null) {
			throw new IllegalStateException("onNewItem cannot be null!");
		}
		this.solutions = Int2ObjectMaps.synchronize(new Int2ObjectOpenHashMap<>());
		this.request = request;
		this.telemetryBaseData = telemetryBaseData;
		this.onNewItem = onNewItem;
	}

	void process(JsonObject json) {
		if (json == null) {
			throw new IllegalStateException("json cannot be null!");
		}
		if (this.request.isCancelled()) {
			return;
		}
		try {
			OpenAICompletionResponse completion = (OpenAICompletionResponse) OpenAI.GSON.fromJson((JsonElement) json,
					OpenAICompletionResponse.class);
			this.completionId = completion.id;
			this.completionCreatedTimestamp = completion.createdTimestamp;
			for (OpenAICompletionResponse.Choice choice : completion.choices) {
				if (this.request.isCancelled()) {
					return;
				}
				int index = choice.index;
				APIJsonDataStreaming solution = (APIJsonDataStreaming) this.solutions.computeIfAbsent(index,
						key -> new APIJsonDataStreaming());
				if (solution == null)
					continue;
				solution.appendText(choice.text);
				OpenAICompletionResponse.Logprobs logprobs = choice.logprobs;
				if (logprobs != null) {
					solution.tokens.add(List.of(logprobs.tokens));
					solution.textOffset.add(IntList.of((int[]) logprobs.textOffset));
					solution.logprobs.add(DoubleList.of((double[]) logprobs.tokenLogprobs));
					solution.topLogprobs.add(logprobs.topLogprobs);
				}
				Integer finishOffset = null;
				if (choice.text.contains("\n") || choice.isFinished() && !choice.text.isEmpty()) {
					if (this.request.isCancelled()) {
						return;
					}
					String trailingWS = CopilotStringUtil.trailingWhitespace(this.request.getLineInfo().getLine());
					String content = this.request.getDocumentContent().substring(0,
							this.request.getDocumentContent().length() - trailingWS.length());
					int offset = this.request.getOffset() - trailingWS.length();
					finishOffset = this.request.getLanguage().findBlockEnd(this.request.getProject(), this.request,
							content, offset, solution.getJoinedText(), choice.isFinished());
				}
				if (this.request.isCancelled()) {
					return;
				}
				if (!choice.isFinished() && finishOffset == null)
					continue;
				this.solutions.replace(index, null);
				Pair<APIChoice, APILogprobs> apiChoicePair = this.prepareSolutionForReturn(solution, finishOffset,
						index);
				if (apiChoicePair == null)
					continue;
				this.onNewItem.accept((APIChoice) apiChoicePair.first);
				this.logCompletionChoice((APIChoice) apiChoicePair.first, (APILogprobs) apiChoicePair.second);
			}
		} catch (JsonSyntaxException e) {
			LOG.error("syntax error parsing completion JSON: " + json, (Throwable) e);
		}
	}

	void close() {
		LOG.trace("close()");
		this.solutions.forEach((index, solution) -> {
			if (solution == null) {
				return;
			}
			Pair<APIChoice, APILogprobs> apiChoicePair = this.prepareSolutionForReturn((APIJsonDataStreaming) solution,
					null, (int) index);
			if (apiChoicePair != null) {
				this.onNewItem.accept((APIChoice) apiChoicePair.first);
				this.logCompletionChoice((APIChoice) apiChoicePair.first, (APILogprobs) apiChoicePair.second);
			}
		});
		this.solutions.clear();
	}

	private Pair<APIChoice, APILogprobs> prepareSolutionForReturn(APIJsonDataStreaming solution, Integer finishOffset,
			int index) {
		if (solution == null) {
			throw new IllegalStateException("solution cannot be null!");
		}
		String completionText = solution.getJoinedText();
		if (finishOffset != null) {
			completionText = completionText.substring(0, finishOffset);
		}
		if (completionText.isEmpty()) {
			return null;
		}
		APILogprobs jsonData = this.convertToAPIJsonData(solution);
		APIChoice apiChoice = this.convertToAPIChoice(completionText, jsonData, index);
		return Pair.create((Object) apiChoice, (Object) jsonData);
	}

	private APIChoice convertToAPIChoice(String completionText, APILogprobs logprobs, int choiceIndex) {
		if (completionText == null) {
			throw new IllegalStateException("completionText cannot be null!");
		}
		if (logprobs == null) {
			throw new IllegalStateException("logprobs cannot be null!");
		}
		CompletionResponseInfo responseInfo = this.completionResponseInfo;
		assert (responseInfo != null);
		String id = this.completionId;
		assert (id != null);
		String[] lines = NEWLINE_PATTERN.split(completionText);
		List<String> fixedLines = EditorRequestUtil.fixIndentation(List.of(lines), this.request.isUseTabIndents(),
				this.request.getTabWidth());
		return new DefaultAPIChoice(responseInfo, fixedLines,
				logprobs.getTokens() == null ? 0 : logprobs.getTokens().size(), choiceIndex,
				this.request.getRequestId(), id, this.completionCreatedTimestamp, logprobs.calculateMeanLogprob(),
				this.telemetryBaseData, false);
	}

	private APILogprobs convertToAPIJsonData(APIJsonDataStreaming data) {
		if (data == null) {
			throw new IllegalStateException("data cannot be null!");
		}
		int numText = data.getText().size();
		int numTokens = data.tokens.size();
		int numOffsets = data.textOffset.size();
		int numLogprobs = data.logprobs.size();
		assert (numText == numTokens);
		assert (numText == numOffsets);
		assert (numText == numLogprobs);
		DoubleList flattenedLogprobs = data.logprobs.stream().filter(Objects::nonNull)
				.reduce((DoubleList) new DoubleArrayList(), (result, list) -> {
					result.addAll(list);
					return result;
				});
		List<StringDoublePair> flattenedTopLogprobs = data.topLogprobs.stream().filter(Objects::nonNull)
				.reduce(new ArrayList(), (result, mapList) -> {
					for (String2DoubleMap map : mapList) {
						map.forEach((key, value) -> result.add(new StringDoublePair((String) key, (double) value)));
					}
					return result;
				}, (listA, listB) -> {
					ArrayList result = new ArrayList(listA.size() + listB.size());
					result.addAll(listA);
					result.addAll(listB);
					return result;
				});
		IntList flattenedOffsets = data.textOffset.stream().filter(Objects::nonNull)
				.reduce((IntList) new IntArrayList(), (result, list) -> {
					result.addAll(list);
					return result;
				});
		List flattenedTokens = data.tokens.stream().filter(Objects::nonNull).reduce(new ArrayList(), (result, list) -> {
			result.addAll(list);
			return result;
		});
		return new APILogprobs((List<Integer>) flattenedOffsets, (List<Double>) flattenedLogprobs, flattenedTopLogprobs,
				flattenedTokens);
	}

	void updateWithResponse(HttpResponse.ResponseInfo responseInfo) {
		if (responseInfo == null) {
			throw new IllegalStateException("responseInfo cannot be null!");
		}
		assert (this.completionResponseInfo == null);
		this.completionResponseInfo = new CompletionResponseInfo(OpenAIHttpUtil.getRequestId(responseInfo),
				OpenAIHttpUtil.getServerExperiments(responseInfo), OpenAIHttpUtil.getProxyRole(responseInfo),
				OpenAIHttpUtil.getModelEndpoint(responseInfo), this.request.getRequestTimestamp(),
				OpenAIHttpUtil.getProcessingTime(responseInfo));
	}

	private void logCompletionChoice(APIChoice apiChoice, APILogprobs logprobs) {
		if (apiChoice == null) {
			throw new IllegalStateException("apiChoice cannot be null!");
		}
		if (logprobs == null) {
			throw new IllegalStateException("logprobs cannot be null!");
		}
		Map<String, String> mapData = Maps.merge(apiChoice.getResponseInfo().createTelemetryData(),
				logprobs.createTelemetryJson(),
				Map.of("completionTextJson", apiChoice.getCompletionText(), "choiceIndex",
						String.valueOf(apiChoice.getChoiceIndex()), "completionId",
						StringUtil.defaultIfEmpty((String) this.completionId, (String) "")));
		TelemetryService.getInstance().trackSecure("engine.completion", TelemetryData.createIssued(mapData));
	}

}
