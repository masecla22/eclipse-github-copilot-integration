/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.concurrency.Promise
 */
package com.github.copilot.lang.agent;

import com.github.copilot.lang.agent.rpc.JsonRpcCommand;
import com.github.copilot.lang.agent.rpc.JsonRpcNotification;
import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.concurrency.Promise;

public interface CopilotAgentProcessService {
    @NotNull
    public static CopilotAgentProcessService getInstance() {
        CopilotAgentProcessService copilotAgentProcessService = (CopilotAgentProcessService)ApplicationManager.getApplication().getService(CopilotAgentProcessService.class);
        if (copilotAgentProcessService == null) {
            CopilotAgentProcessService.$$$reportNull$$$0(0);
        }
        return copilotAgentProcessService;
    }

    public boolean isSupported();

    @NotNull
    public <T> Promise<T> executeCommand(@NotNull JsonRpcCommand<T> var1);

    public void executeNotification(@NotNull JsonRpcNotification var1);

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "com/github/copilot/lang/agent/CopilotAgentProcessService", "getInstance"));
    }
}

