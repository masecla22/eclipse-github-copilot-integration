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
import org.jetbrains.concurrency.Promise;

public interface CopilotAgentProcessService {
	public static CopilotAgentProcessService getInstance() {
		CopilotAgentProcessService copilotAgentProcessService = (CopilotAgentProcessService) ApplicationManager
				.getApplication().getService(CopilotAgentProcessService.class);
		if (copilotAgentProcessService == null) {
			throw new IllegalStateException("copilotAgentProcessService cannot be null!");
		}
		return copilotAgentProcessService;
	}

	public boolean isSupported();

	public <T> Promise<T> executeCommand(JsonRpcCommand<T> var1);

	public void executeNotification(JsonRpcNotification var1);

}
