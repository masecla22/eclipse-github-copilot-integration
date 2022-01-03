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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.Promise;

public class CopilotAgentCompletionService
implements CopilotCompletionService {
    private static final Logger LOG = Logger.getInstance(CopilotAgentCompletionService.class);
    protected final CompletionCache cache = new SimpleCompletionCache(32);

    @Override
    public boolean isAvailable(@NotNull Editor editor) {
        if (editor == null) {
            CopilotAgentCompletionService.$$$reportNull$$$0(0);
        }
        return true;
    }

    @Override
    @Nullable
    public EditorRequest createRequest(@NotNull Editor editor, int offset, @NotNull CompletionType completionType) {
        if (editor == null) {
            CopilotAgentCompletionService.$$$reportNull$$$0(1);
        }
        if (completionType == null) {
            CopilotAgentCompletionService.$$$reportNull$$$0(2);
        }
        return AgentEditorRequest.create(editor, offset, completionType);
    }

    @Override
    @Nullable
    public List<CopilotInlayList> fetchCachedCompletions(@NotNull EditorRequest request) {
        if (request == null) {
            CopilotAgentCompletionService.$$$reportNull$$$0(3);
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
        List<CopilotInlayList> inlays = items.stream().map(item -> CompletionUtil.createEditorCompletion(request, item, dropLinePrefix)).filter(Objects::nonNull).collect(Collectors.toList());
        return inlays.isEmpty() ? null : inlays;
    }

    @Override
    public boolean fetchCompletions(@NotNull EditorRequest request, @Nullable GitHubCopilotToken proxyToken, @Nullable Integer maxCompletions, boolean enableCaching, boolean cycling, @NotNull Flow.Subscriber<List<CopilotInlayList>> subscriber) {
        if (request == null) {
            CopilotAgentCompletionService.$$$reportNull$$$0(4);
        }
        if (subscriber == null) {
            CopilotAgentCompletionService.$$$reportNull$$$0(5);
        }
        Language language = request.getFileLanguage();
        String languageId = VSCodeLanguageMap.INTELLIJ_VSCODE_MAP.getOrDefault(language.getID(), language.getID());
        Document doc = new Document(request.getDocumentContent(), languageId, new Position(request.getLineInfo()), !request.isUseTabIndents(), request.getTabWidth(), request.getRelativeFilePath(), request.getRelativeFilePath());
        GetCompletionsCommand command = cycling ? new GetCompletionsCyclingCommand(doc, null) : new GetCompletionsCommand(doc, null);
        Promise<GetCompletionsResult> promise = CopilotAgentProcessService.getInstance().executeCommand(command);
        if (promise.getState() == Promise.State.REJECTED) {
            LOG.warn("promise was rejected: " + promise);
            try (SubmissionPublisher<List<CopilotInlayList>> publisher = new SubmissionPublisher<List<CopilotInlayList>>();){
                publisher.subscribe(subscriber);
                publisher.closeExceptionally(new IllegalStateException("promise was rejected"));
            }
            return false;
        }
        promise.onError(throwable -> {
            try (SubmissionPublisher publisher = new SubmissionPublisher();){
                publisher.subscribe(subscriber);
                publisher.closeExceptionally((Throwable)throwable);
            }
        });
        promise.onSuccess(result -> {
            try (SubmissionPublisher publisher = new SubmissionPublisher();){
                publisher.subscribe(subscriber);
                ArrayList<AgentCompletionList> inlayLists = new ArrayList<AgentCompletionList>();
                List<GetCompletionsResult.Completion> completions = result.getCompletions();
                for (GetCompletionsResult.Completion completion : completions) {
                    AgentCompletion apiChoice = new AgentCompletion(completion);
                    this.cache.add(request.getCurrentDocumentPrefix(), request.getCurrentDocumentPrefix(), true, apiChoice);
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
    public boolean isSupportingOnDemandCycling(@NotNull Editor editor) {
        if (editor == null) {
            CopilotAgentCompletionService.$$$reportNull$$$0(6);
        }
        return true;
    }

    @Override
    public boolean isCyclingReplacingCompletions() {
        return true;
    }

    @Override
    public void sendShownTelemetry(@NotNull CopilotCompletion completion) {
        if (completion == null) {
            CopilotAgentCompletionService.$$$reportNull$$$0(7);
        }
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            String uuid = ((AgentCompletion)completion).getAgentData().getUuid();
            CopilotAgentProcessService.getInstance().executeCommand(new NotifyShownCommand(uuid));
        });
    }

    @Override
    public void sendAcceptedTelemetry(@NotNull CopilotCompletion completion, @NotNull CompletionType completionType) {
        if (completion == null) {
            CopilotAgentCompletionService.$$$reportNull$$$0(8);
        }
        if (completionType == null) {
            CopilotAgentCompletionService.$$$reportNull$$$0(9);
        }
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            String uuid = ((AgentCompletion)completion).getAgentData().getUuid();
            CopilotAgentProcessService.getInstance().executeCommand(new NotifyAcceptedCommand(uuid));
        });
    }

    @Override
    public void sendRejectedTelemetry(@NotNull List<CopilotCompletion> completions) {
        if (completions == null) {
            CopilotAgentCompletionService.$$$reportNull$$$0(10);
        }
        if (completions.isEmpty()) {
            return;
        }
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            List<String> uuids = completions.stream().map(i -> ((AgentCompletion)i).getAgentData().getUuid()).collect(Collectors.toList());
            CopilotAgentProcessService.getInstance().executeCommand(new NotifyRejectedCommand(uuids));
        });
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2;
        Object[] objectArray3 = new Object[3];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "editor";
                break;
            }
            case 2: 
            case 9: {
                objectArray2 = objectArray3;
                objectArray3[0] = "completionType";
                break;
            }
            case 3: 
            case 4: {
                objectArray2 = objectArray3;
                objectArray3[0] = "request";
                break;
            }
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "subscriber";
                break;
            }
            case 7: 
            case 8: {
                objectArray2 = objectArray3;
                objectArray3[0] = "completion";
                break;
            }
            case 10: {
                objectArray2 = objectArray3;
                objectArray3[0] = "completions";
                break;
            }
        }
        objectArray2[1] = "com/github/copilot/lang/agent/CopilotAgentCompletionService";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "isAvailable";
                break;
            }
            case 1: 
            case 2: {
                objectArray = objectArray2;
                objectArray2[2] = "createRequest";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[2] = "fetchCachedCompletions";
                break;
            }
            case 4: 
            case 5: {
                objectArray = objectArray2;
                objectArray2[2] = "fetchCompletions";
                break;
            }
            case 6: {
                objectArray = objectArray2;
                objectArray2[2] = "isSupportingOnDemandCycling";
                break;
            }
            case 7: {
                objectArray = objectArray2;
                objectArray2[2] = "sendShownTelemetry";
                break;
            }
            case 8: 
            case 9: {
                objectArray = objectArray2;
                objectArray2[2] = "sendAcceptedTelemetry";
                break;
            }
            case 10: {
                objectArray = objectArray2;
                objectArray2[2] = "sendRejectedTelemetry";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objectArray));
    }

    private static class AgentCompletionList
    implements CopilotInlayList {
        private final CopilotInlayList inlays;
        private final AgentCompletion completion;
        @NotNull
        private final EditorRequest request;

        public AgentCompletionList(@Nullable CopilotInlayList inlays, @NotNull AgentCompletion completion, @NotNull EditorRequest request) {
            if (completion == null) {
                AgentCompletionList.$$$reportNull$$$0(0);
            }
            if (request == null) {
                AgentCompletionList.$$$reportNull$$$0(1);
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
        @NotNull
        public CopilotCompletion getCopilotCompletion() {
            AgentCompletion agentCompletion = this.completion;
            if (agentCompletion == null) {
                AgentCompletionList.$$$reportNull$$$0(2);
            }
            return agentCompletion;
        }

        @Override
        @NotNull
        public TextRange getReplacementRange() {
            String text = this.request.getDocumentContent();
            Range range = this.completion.getAgentData().getRange();
            int startOffset = range.getStart().toOffset(text);
            int endOffset = range.getEnd().toOffset(text);
            assert (startOffset >= 0);
            assert (endOffset >= startOffset);
            TextRange textRange = TextRange.create((int)startOffset, (int)endOffset);
            if (textRange == null) {
                AgentCompletionList.$$$reportNull$$$0(3);
            }
            return textRange;
        }

        @Override
        @NotNull
        public String getReplacementText() {
            String string = this.completion.getAgentData().getText();
            if (string == null) {
                AgentCompletionList.$$$reportNull$$$0(4);
            }
            return string;
        }

        @Override
        @NotNull
        public List<CopilotEditorInlay> getInlays() {
            List<CopilotEditorInlay> list = this.inlays == null ? Collections.emptyList() : this.inlays.getInlays();
            if (list == null) {
                AgentCompletionList.$$$reportNull$$$0(5);
            }
            return list;
        }

        @Override
        @NotNull
        public Iterator<CopilotEditorInlay> iterator() {
            Iterator<CopilotEditorInlay> iterator = this.inlays != null ? this.inlays.iterator() : Collections.emptyIterator();
            if (iterator == null) {
                AgentCompletionList.$$$reportNull$$$0(6);
            }
            return iterator;
        }

        private static /* synthetic */ void $$$reportNull$$$0(int n) {
            RuntimeException runtimeException;
            Object[] objectArray;
            Object[] objectArray2;
            int n2;
            String string;
            switch (n) {
                default: {
                    string = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                    break;
                }
                case 2: 
                case 3: 
                case 4: 
                case 5: 
                case 6: {
                    string = "@NotNull method %s.%s must not return null";
                    break;
                }
            }
            switch (n) {
                default: {
                    n2 = 3;
                    break;
                }
                case 2: 
                case 3: 
                case 4: 
                case 5: 
                case 6: {
                    n2 = 2;
                    break;
                }
            }
            Object[] objectArray3 = new Object[n2];
            switch (n) {
                default: {
                    objectArray2 = objectArray3;
                    objectArray3[0] = "completion";
                    break;
                }
                case 1: {
                    objectArray2 = objectArray3;
                    objectArray3[0] = "request";
                    break;
                }
                case 2: 
                case 3: 
                case 4: 
                case 5: 
                case 6: {
                    objectArray2 = objectArray3;
                    objectArray3[0] = "com/github/copilot/lang/agent/CopilotAgentCompletionService$AgentCompletionList";
                    break;
                }
            }
            switch (n) {
                default: {
                    objectArray = objectArray2;
                    objectArray2[1] = "com/github/copilot/lang/agent/CopilotAgentCompletionService$AgentCompletionList";
                    break;
                }
                case 2: {
                    objectArray = objectArray2;
                    objectArray2[1] = "getCopilotCompletion";
                    break;
                }
                case 3: {
                    objectArray = objectArray2;
                    objectArray2[1] = "getReplacementRange";
                    break;
                }
                case 4: {
                    objectArray = objectArray2;
                    objectArray2[1] = "getReplacementText";
                    break;
                }
                case 5: {
                    objectArray = objectArray2;
                    objectArray2[1] = "getInlays";
                    break;
                }
                case 6: {
                    objectArray = objectArray2;
                    objectArray2[1] = "iterator";
                    break;
                }
            }
            switch (n) {
                default: {
                    objectArray = objectArray;
                    objectArray[2] = "<init>";
                    break;
                }
                case 2: 
                case 3: 
                case 4: 
                case 5: 
                case 6: {
                    break;
                }
            }
            String string2 = String.format(string, objectArray);
            switch (n) {
                default: {
                    runtimeException = new IllegalArgumentException(string2);
                    break;
                }
                case 2: 
                case 3: 
                case 4: 
                case 5: 
                case 6: {
                    runtimeException = new IllegalStateException(string2);
                    break;
                }
            }
            throw runtimeException;
        }
    }
}

