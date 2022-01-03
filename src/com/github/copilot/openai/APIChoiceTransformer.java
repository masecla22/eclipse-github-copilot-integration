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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class APIChoiceTransformer {
    private static final Logger LOG = Logger.getInstance(APIChoiceTransformer.class);
    private static final Pattern NEWLINE_PATTERN = Pattern.compile("\n");
    @NotNull
    private final LanguageEditorRequest request;
    @NotNull
    private final TelemetryData telemetryBaseData;
    @NotNull
    private final Consumer<APIChoice> onNewItem;
    private final Int2ObjectMap<APIJsonDataStreaming> solutions;
    @Nullable
    private volatile String completionId;
    private volatile int completionCreatedTimestamp;
    @Nullable
    private volatile CompletionResponseInfo completionResponseInfo;

    APIChoiceTransformer(@NotNull LanguageEditorRequest request, @NotNull TelemetryData telemetryBaseData, @NotNull Consumer<APIChoice> onNewItem) {
        if (request == null) {
            APIChoiceTransformer.$$$reportNull$$$0(0);
        }
        if (telemetryBaseData == null) {
            APIChoiceTransformer.$$$reportNull$$$0(1);
        }
        if (onNewItem == null) {
            APIChoiceTransformer.$$$reportNull$$$0(2);
        }
        this.solutions = Int2ObjectMaps.synchronize((Int2ObjectMap)new Int2ObjectOpenHashMap());
        this.request = request;
        this.telemetryBaseData = telemetryBaseData;
        this.onNewItem = onNewItem;
    }

    void process(@NotNull JsonObject json) {
        if (json == null) {
            APIChoiceTransformer.$$$reportNull$$$0(3);
        }
        if (this.request.isCancelled()) {
            return;
        }
        try {
            OpenAICompletionResponse completion = (OpenAICompletionResponse)OpenAI.GSON.fromJson((JsonElement)json, OpenAICompletionResponse.class);
            this.completionId = completion.id;
            this.completionCreatedTimestamp = completion.createdTimestamp;
            for (OpenAICompletionResponse.Choice choice : completion.choices) {
                if (this.request.isCancelled()) {
                    return;
                }
                int index = choice.index;
                APIJsonDataStreaming solution = (APIJsonDataStreaming)this.solutions.computeIfAbsent(index, key -> new APIJsonDataStreaming());
                if (solution == null) continue;
                solution.appendText(choice.text);
                OpenAICompletionResponse.Logprobs logprobs = choice.logprobs;
                if (logprobs != null) {
                    solution.tokens.add(List.of(logprobs.tokens));
                    solution.textOffset.add(IntList.of((int[])logprobs.textOffset));
                    solution.logprobs.add(DoubleList.of((double[])logprobs.tokenLogprobs));
                    solution.topLogprobs.add(logprobs.topLogprobs);
                }
                Integer finishOffset = null;
                if (choice.text.contains("\n") || choice.isFinished() && !choice.text.isEmpty()) {
                    if (this.request.isCancelled()) {
                        return;
                    }
                    String trailingWS = CopilotStringUtil.trailingWhitespace(this.request.getLineInfo().getLine());
                    String content = this.request.getDocumentContent().substring(0, this.request.getDocumentContent().length() - trailingWS.length());
                    int offset = this.request.getOffset() - trailingWS.length();
                    finishOffset = this.request.getLanguage().findBlockEnd(this.request.getProject(), this.request, content, offset, solution.getJoinedText(), choice.isFinished());
                }
                if (this.request.isCancelled()) {
                    return;
                }
                if (!choice.isFinished() && finishOffset == null) continue;
                this.solutions.replace(index, null);
                Pair<APIChoice, APILogprobs> apiChoicePair = this.prepareSolutionForReturn(solution, finishOffset, index);
                if (apiChoicePair == null) continue;
                this.onNewItem.accept((APIChoice)apiChoicePair.first);
                this.logCompletionChoice((APIChoice)apiChoicePair.first, (APILogprobs)apiChoicePair.second);
            }
        }
        catch (JsonSyntaxException e) {
            LOG.error("syntax error parsing completion JSON: " + json, (Throwable)e);
        }
    }

    void close() {
        LOG.trace("close()");
        this.solutions.forEach((index, solution) -> {
            if (solution == null) {
                return;
            }
            Pair<APIChoice, APILogprobs> apiChoicePair = this.prepareSolutionForReturn((APIJsonDataStreaming)solution, null, (int)index);
            if (apiChoicePair != null) {
                this.onNewItem.accept((APIChoice)apiChoicePair.first);
                this.logCompletionChoice((APIChoice)apiChoicePair.first, (APILogprobs)apiChoicePair.second);
            }
        });
        this.solutions.clear();
    }

    private Pair<APIChoice, APILogprobs> prepareSolutionForReturn(@NotNull APIJsonDataStreaming solution, @Nullable Integer finishOffset, int index) {
        if (solution == null) {
            APIChoiceTransformer.$$$reportNull$$$0(4);
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
        return Pair.create((Object)apiChoice, (Object)jsonData);
    }

    private APIChoice convertToAPIChoice(@NotNull String completionText, @NotNull APILogprobs logprobs, int choiceIndex) {
        if (completionText == null) {
            APIChoiceTransformer.$$$reportNull$$$0(5);
        }
        if (logprobs == null) {
            APIChoiceTransformer.$$$reportNull$$$0(6);
        }
        CompletionResponseInfo responseInfo = this.completionResponseInfo;
        assert (responseInfo != null);
        String id = this.completionId;
        assert (id != null);
        String[] lines = NEWLINE_PATTERN.split(completionText);
        List<String> fixedLines = EditorRequestUtil.fixIndentation(List.of(lines), this.request.isUseTabIndents(), this.request.getTabWidth());
        return new DefaultAPIChoice(responseInfo, fixedLines, logprobs.getTokens() == null ? 0 : logprobs.getTokens().size(), choiceIndex, this.request.getRequestId(), id, this.completionCreatedTimestamp, logprobs.calculateMeanLogprob(), this.telemetryBaseData, false);
    }

    @NotNull
    private APILogprobs convertToAPIJsonData(@NotNull APIJsonDataStreaming data) {
        if (data == null) {
            APIChoiceTransformer.$$$reportNull$$$0(7);
        }
        int numText = data.getText().size();
        int numTokens = data.tokens.size();
        int numOffsets = data.textOffset.size();
        int numLogprobs = data.logprobs.size();
        assert (numText == numTokens);
        assert (numText == numOffsets);
        assert (numText == numLogprobs);
        DoubleList flattenedLogprobs = data.logprobs.stream().filter(Objects::nonNull).reduce((DoubleList)new DoubleArrayList(), (result, list) -> {
            result.addAll(list);
            return result;
        });
        List flattenedTopLogprobs = data.topLogprobs.stream().filter(Objects::nonNull).reduce(new ArrayList(), (result, mapList) -> {
            for (String2DoubleMap map : mapList) {
                map.forEach((key, value) -> result.add(new StringDoublePair((String)key, (double)value)));
            }
            return result;
        }, (listA, listB) -> {
            ArrayList result = new ArrayList(listA.size() + listB.size());
            result.addAll(listA);
            result.addAll(listB);
            return result;
        });
        IntList flattenedOffsets = data.textOffset.stream().filter(Objects::nonNull).reduce((IntList)new IntArrayList(), (result, list) -> {
            result.addAll(list);
            return result;
        });
        List flattenedTokens = data.tokens.stream().filter(Objects::nonNull).reduce(new ArrayList(), (result, list) -> {
            result.addAll(list);
            return result;
        });
        return new APILogprobs((List<Integer>)flattenedOffsets, (List<Double>)flattenedLogprobs, flattenedTopLogprobs, flattenedTokens);
    }

    void updateWithResponse(@NotNull HttpResponse.ResponseInfo responseInfo) {
        if (responseInfo == null) {
            APIChoiceTransformer.$$$reportNull$$$0(8);
        }
        assert (this.completionResponseInfo == null);
        this.completionResponseInfo = new CompletionResponseInfo(OpenAIHttpUtil.getRequestId(responseInfo), OpenAIHttpUtil.getServerExperiments(responseInfo), OpenAIHttpUtil.getProxyRole(responseInfo), OpenAIHttpUtil.getModelEndpoint(responseInfo), this.request.getRequestTimestamp(), OpenAIHttpUtil.getProcessingTime(responseInfo));
    }

    private void logCompletionChoice(@NotNull APIChoice apiChoice, @NotNull APILogprobs logprobs) {
        if (apiChoice == null) {
            APIChoiceTransformer.$$$reportNull$$$0(9);
        }
        if (logprobs == null) {
            APIChoiceTransformer.$$$reportNull$$$0(10);
        }
        Map<String, String> mapData = Maps.merge(apiChoice.getResponseInfo().createTelemetryData(), logprobs.createTelemetryJson(), Map.of("completionTextJson", apiChoice.getCompletionText(), "choiceIndex", String.valueOf(apiChoice.getChoiceIndex()), "completionId", StringUtil.defaultIfEmpty((String)this.completionId, (String)"")));
        TelemetryService.getInstance().trackSecure("engine.completion", TelemetryData.createIssued(mapData));
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2;
        Object[] objectArray3 = new Object[3];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "request";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "telemetryBaseData";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "onNewItem";
                break;
            }
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "json";
                break;
            }
            case 4: {
                objectArray2 = objectArray3;
                objectArray3[0] = "solution";
                break;
            }
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "completionText";
                break;
            }
            case 6: 
            case 10: {
                objectArray2 = objectArray3;
                objectArray3[0] = "logprobs";
                break;
            }
            case 7: {
                objectArray2 = objectArray3;
                objectArray3[0] = "data";
                break;
            }
            case 8: {
                objectArray2 = objectArray3;
                objectArray3[0] = "responseInfo";
                break;
            }
            case 9: {
                objectArray2 = objectArray3;
                objectArray3[0] = "apiChoice";
                break;
            }
        }
        objectArray2[1] = "com/github/copilot/openai/APIChoiceTransformer";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "<init>";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[2] = "process";
                break;
            }
            case 4: {
                objectArray = objectArray2;
                objectArray2[2] = "prepareSolutionForReturn";
                break;
            }
            case 5: 
            case 6: {
                objectArray = objectArray2;
                objectArray2[2] = "convertToAPIChoice";
                break;
            }
            case 7: {
                objectArray = objectArray2;
                objectArray2[2] = "convertToAPIJsonData";
                break;
            }
            case 8: {
                objectArray = objectArray2;
                objectArray2[2] = "updateWithResponse";
                break;
            }
            case 9: 
            case 10: {
                objectArray = objectArray2;
                objectArray2[2] = "logCompletionChoice";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objectArray));
    }
}

