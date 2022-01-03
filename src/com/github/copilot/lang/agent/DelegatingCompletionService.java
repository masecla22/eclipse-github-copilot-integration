/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.editor.Editor
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang.agent;

import com.github.copilot.completions.CopilotCompletion;
import com.github.copilot.completions.CopilotCompletionService;
import com.github.copilot.completions.CopilotInlayList;
import com.github.copilot.completions.DefaultCopilotCompletionService;
import com.github.copilot.github.GitHubCopilotToken;
import com.github.copilot.lang.agent.CopilotAgent;
import com.github.copilot.lang.agent.CopilotAgentCompletionService;
import com.github.copilot.request.CompletionType;
import com.github.copilot.request.EditorRequest;
import com.intellij.openapi.editor.Editor;
import java.util.List;
import java.util.concurrent.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DelegatingCompletionService
implements CopilotCompletionService {
    private final CopilotAgentCompletionService agentService = new CopilotAgentCompletionService();
    private final DefaultCopilotCompletionService defaultService = new DefaultCopilotCompletionService();

    @Override
    public boolean isAvailable(@NotNull Editor editor) {
        if (editor == null) {
            DelegatingCompletionService.$$$reportNull$$$0(0);
        }
        return this.getDelegate().isAvailable(editor);
    }

    @Override
    @Nullable
    public EditorRequest createRequest(@NotNull Editor editor, int offset, @NotNull CompletionType completionType) {
        if (editor == null) {
            DelegatingCompletionService.$$$reportNull$$$0(1);
        }
        if (completionType == null) {
            DelegatingCompletionService.$$$reportNull$$$0(2);
        }
        return this.getDelegate().createRequest(editor, offset, completionType);
    }

    @Override
    public boolean fetchCompletions(@NotNull EditorRequest request, @Nullable GitHubCopilotToken proxyToken, @Nullable Integer maxCompletions, boolean enableCaching, boolean cycling, @NotNull Flow.Subscriber<List<CopilotInlayList>> subscriber) {
        if (request == null) {
            DelegatingCompletionService.$$$reportNull$$$0(3);
        }
        if (subscriber == null) {
            DelegatingCompletionService.$$$reportNull$$$0(4);
        }
        return this.getDelegate().fetchCompletions(request, proxyToken, maxCompletions, enableCaching, cycling, subscriber);
    }

    @Override
    @Nullable
    public List<CopilotInlayList> fetchCachedCompletions(@NotNull EditorRequest request) {
        if (request == null) {
            DelegatingCompletionService.$$$reportNull$$$0(5);
        }
        return this.getDelegate().fetchCachedCompletions(request);
    }

    @Override
    public void reset() {
        this.getDelegate().reset();
    }

    @Override
    public boolean isSupportingOnDemandCycling(@NotNull Editor editor) {
        if (editor == null) {
            DelegatingCompletionService.$$$reportNull$$$0(6);
        }
        return this.getDelegate().isSupportingOnDemandCycling(editor);
    }

    @Override
    public boolean isCyclingReplacingCompletions() {
        return this.getDelegate().isCyclingReplacingCompletions();
    }

    private CopilotCompletionService getDelegate() {
        return CopilotAgent.isAgentSupportedAndEnabled() ? this.agentService : this.defaultService;
    }

    @Override
    public void sendShownTelemetry(@NotNull CopilotCompletion completion) {
        if (completion == null) {
            DelegatingCompletionService.$$$reportNull$$$0(7);
        }
        this.getDelegate().sendShownTelemetry(completion);
    }

    @Override
    public void sendAcceptedTelemetry(@NotNull CopilotCompletion completion, @NotNull CompletionType completionType) {
        if (completion == null) {
            DelegatingCompletionService.$$$reportNull$$$0(8);
        }
        if (completionType == null) {
            DelegatingCompletionService.$$$reportNull$$$0(9);
        }
        this.getDelegate().sendAcceptedTelemetry(completion, completionType);
    }

    @Override
    public void sendRejectedTelemetry(@NotNull List<CopilotCompletion> completions) {
        if (completions == null) {
            DelegatingCompletionService.$$$reportNull$$$0(10);
        }
        this.getDelegate().sendRejectedTelemetry(completions);
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
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "request";
                break;
            }
            case 4: {
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
        objectArray2[1] = "com/github/copilot/lang/agent/DelegatingCompletionService";
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
            case 3: 
            case 4: {
                objectArray = objectArray2;
                objectArray2[2] = "fetchCompletions";
                break;
            }
            case 5: {
                objectArray = objectArray2;
                objectArray2[2] = "fetchCachedCompletions";
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
}

