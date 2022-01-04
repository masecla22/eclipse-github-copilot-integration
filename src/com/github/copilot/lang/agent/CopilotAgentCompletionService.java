/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.lang.Language
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.util.TextRange
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.concurrency.Promise
 *  org.jetbrains.concurrency.Promise$State
 */
package com.github.copilot.lang.agent;

import com.github.copilot.completions.CompletionCache;
import com.github.copilot.completions.CompletionUtil;
import com.github.copilot.completions.CopilotCompletion;
import com.github.copilot.completions.CopilotCompletionService;
import com.github.copilot.completions.CopilotEditorInlay;
import com.github.copilot.completions.CopilotInlayList;
import com.github.copilot.completions.SimpleCompletionCache;
import com.github.copilot.github.GitHubCopilotToken;
import com.github.copilot.lang.agent.AgentCompletion;
import com.github.copilot.lang.agent.AgentEditorRequest;
import com.github.copilot.lang.agent.CopilotAgentProcessService;
import com.github.copilot.lang.agent.commands.Document;
import com.github.copilot.lang.agent.commands.GetCompletionsCommand;
import com.github.copilot.lang.agent.commands.GetCompletionsCyclingCommand;
import com.github.copilot.lang.agent.commands.GetCompletionsResult;
import com.github.copilot.lang.agent.commands.NotifyAcceptedCommand;
import com.github.copilot.lang.agent.commands.NotifyRejectedCommand;
import com.github.copilot.lang.agent.commands.NotifyShownCommand;
import com.github.copilot.lang.agent.commands.Position;
import com.github.copilot.lang.agent.commands.Range;
import com.github.copilot.lang.fallback.VSCodeLanguageMap;
import com.github.copilot.request.CompletionType;
import com.github.copilot.request.EditorRequest;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.Collectors;
import org.jetbrains.concurrency.Promise;

public class CopilotAgentCompletionService implements CopilotCompletionService {
	private static final Logger LOG = Logger.getInstance(CopilotAgentCompletionService.class);
	protected final CompletionCache cache = new SimpleCompletionCache(32);

	@Override
	public boolean isAvailable(Editor editor) {
		if (editor == null) {
			throw new IllegalStateException("editor cannot be null!");
		}
		return true;
	}

	@Override
	public EditorRequest createRequest(Editor editor, int offset, CompletionType completionType) {
		if (editor == null) {
			throw new IllegalStateException("editor cannot be null!");
		}
		if (completionType == null) {
			throw new IllegalStateException("completionType cannot be null!");
		}
		return AgentEditorRequest.create(editor, offset, completionType);
	}

	@Override
	public List<CopilotInlayList> fetchCachedCompletions(EditorRequest request) {
		if (request == null) {
			throw new IllegalStateException("request cannot be null!");
		}
		if (CopilotApplicationSettings.settings().internalDisableHttpCache) {
			return null;
		}
		String prefix = request.getCurrentDocumentPrefix();
		List<CopilotCompletion> items = this.cache.getLatest(prefix);
		if (items == null) {
			return null;
		}
		boolean dropLinePrefix = this.cache.isLatestPrefix(prefix);
		List<CopilotInlayList> inlays = items.stream()
				.map(item -> CompletionUtil.createEditorCompletion(request, item, dropLinePrefix))
				.filter(Objects::nonNull).collect(Collectors.toList());
		return inlays.isEmpty() ? null : inlays;
	}

	@Override
	public boolean fetchCompletions(EditorRequest request, GitHubCopilotToken proxyToken, Integer maxCompletions,
			boolean enableCaching, boolean cycling, Flow.Subscriber<List<CopilotInlayList>> subscriber) {
		if (request == null) {
			throw new IllegalStateException("request cannot be null!");
		}
		if (subscriber == null) {
			throw new IllegalStateException("subscriber cannot be null!");
		}
		Language language = request.getFileLanguage();
		String languageId = VSCodeLanguageMap.INTELLIJ_VSCODE_MAP.getOrDefault(language.getID(), language.getID());
		Document doc = new Document(request.getDocumentContent(), languageId, new Position(request.getLineInfo()),
				!request.isUseTabIndents(), request.getTabWidth(), request.getRelativeFilePath(),
				request.getRelativeFilePath());
		GetCompletionsCommand command = cycling ? new GetCompletionsCyclingCommand(doc, null)
				: new GetCompletionsCommand(doc, null);
		Promise<GetCompletionsResult> promise = CopilotAgentProcessService.getInstance().executeCommand(command);
		if (promise.getState() == Promise.State.REJECTED) {
			LOG.warn("promise was rejected: " + promise);
			try (SubmissionPublisher<List<CopilotInlayList>> publisher = new SubmissionPublisher<List<CopilotInlayList>>();) {
				publisher.subscribe(subscriber);
				publisher.closeExceptionally(new IllegalStateException("promise was rejected"));
			}
			return false;
		}
		promise.onError(throwable -> {
			try (SubmissionPublisher publisher = new SubmissionPublisher();) {
				publisher.subscribe(subscriber);
				publisher.closeExceptionally((Throwable) throwable);
			}
		});
		promise.onSuccess(result -> {
			try (SubmissionPublisher publisher = new SubmissionPublisher();) {
				publisher.subscribe(subscriber);
				ArrayList<AgentCompletionList> inlayLists = new ArrayList<AgentCompletionList>();
				List<GetCompletionsResult.Completion> completions = result.getCompletions();
				for (GetCompletionsResult.Completion completion : completions) {
					AgentCompletion apiChoice = new AgentCompletion(completion);
					this.cache.add(request.getCurrentDocumentPrefix(), request.getCurrentDocumentPrefix(), true,
							apiChoice);
					CopilotInlayList inlays = CompletionUtil.createEditorCompletion(request, apiChoice, true);
					inlayLists.add(new AgentCompletionList(inlays, apiChoice, request));
				}
				publisher.submit(inlayLists);
			}
		});
		return true;
	}

	@Override
	public void reset() {
	}

	@Override
	public boolean isSupportingOnDemandCycling(Editor editor) {
		if (editor == null) {
			throw new IllegalStateException("editor cannot be null!");
		}
		return true;
	}

	@Override
	public boolean isCyclingReplacingCompletions() {
		return true;
	}

	@Override
	public void sendShownTelemetry(CopilotCompletion completion) {
		if (completion == null) {
			throw new IllegalStateException("completion cannot be null!");
		}
		ApplicationManager.getApplication().executeOnPooledThread(() -> {
			String uuid = ((AgentCompletion) completion).getAgentData().getUuid();
			CopilotAgentProcessService.getInstance().executeCommand(new NotifyShownCommand(uuid));
		});
	}

	@Override
	public void sendAcceptedTelemetry(CopilotCompletion completion, CompletionType completionType) {
		if (completion == null) {
			throw new IllegalStateException("completion cannot be null!");
		}
		if (completionType == null) {
			throw new IllegalStateException("completionType cannot be null!");
		}
		ApplicationManager.getApplication().executeOnPooledThread(() -> {
			String uuid = ((AgentCompletion) completion).getAgentData().getUuid();
			CopilotAgentProcessService.getInstance().executeCommand(new NotifyAcceptedCommand(uuid));
		});
	}

	@Override
	public void sendRejectedTelemetry(List<CopilotCompletion> completions) {
		if (completions == null) {
			throw new IllegalStateException("completions cannot be null!");
		}
		if (completions.isEmpty()) {
			return;
		}
		ApplicationManager.getApplication().executeOnPooledThread(() -> {
			List<String> uuids = completions.stream().map(i -> ((AgentCompletion) i).getAgentData().getUuid())
					.collect(Collectors.toList());
			CopilotAgentProcessService.getInstance().executeCommand(new NotifyRejectedCommand(uuids));
		});
	}

	private static class AgentCompletionList implements CopilotInlayList {
		private final CopilotInlayList inlays;
		private final AgentCompletion completion;
		private final EditorRequest request;

		public AgentCompletionList(CopilotInlayList inlays, AgentCompletion completion, EditorRequest request) {
			if (completion == null) {
				throw new IllegalStateException("completion cannot be null!");
			}
			if (request == null) {
				throw new IllegalStateException("request cannot be null!");
			}
			this.inlays = inlays;
			this.completion = completion;
			this.request = request;
		}

		@Override
		public boolean isEmpty() {
			return this.inlays == null || this.inlays.isEmpty();
		}

		@Override
		public CopilotCompletion getCopilotCompletion() {
			AgentCompletion agentCompletion = this.completion;
			if (agentCompletion == null) {
				throw new IllegalStateException("agentCompletion cannot be null!");
			}
			return agentCompletion;
		}

		@Override
		public TextRange getReplacementRange() {
			String text = this.request.getDocumentContent();
			Range range = this.completion.getAgentData().getRange();
			int startOffset = range.getStart().toOffset(text);
			int endOffset = range.getEnd().toOffset(text);
			assert (startOffset >= 0);
			assert (endOffset >= startOffset);
			TextRange textRange = TextRange.create((int) startOffset, (int) endOffset);
			if (textRange == null) {
				throw new IllegalStateException("textRange cannot be null!");
			}
			return textRange;
		}

		@Override
		public String getReplacementText() {
			String string = this.completion.getAgentData().getText();
			if (string == null) {
				throw new IllegalStateException("string cannot be null!");
			}
			return string;
		}

		@Override
		public List<CopilotEditorInlay> getInlays() {
			List<CopilotEditorInlay> list = this.inlays == null ? Collections.emptyList() : this.inlays.getInlays();
			if (list == null) {
				throw new IllegalStateException("list cannot be null!");
			}
			return list;
		}

		@Override
		public Iterator<CopilotEditorInlay> iterator() {
			Iterator<CopilotEditorInlay> iterator = this.inlays != null ? this.inlays.iterator()
					: Collections.emptyIterator();
			if (iterator == null) {
				throw new IllegalStateException("iterator cannot be null!");
			}
			return iterator;
		}
	}
}
