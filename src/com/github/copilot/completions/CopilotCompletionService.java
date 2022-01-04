/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.util.concurrency.annotations.RequiresBackgroundThread
 *  com.intellij.util.concurrency.annotations.RequiresEdt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.completions;

import com.github.copilot.completions.CopilotCompletion;
import com.github.copilot.completions.CopilotInlayList;
import com.github.copilot.github.GitHubCopilotToken;
import com.github.copilot.request.CompletionType;
import com.github.copilot.request.EditorRequest;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import com.intellij.util.concurrency.annotations.RequiresEdt;
import java.util.List;
import java.util.concurrent.Flow;

public interface CopilotCompletionService {
	public static CopilotCompletionService getInstance() {
		return (CopilotCompletionService) ApplicationManager.getApplication()
				.getService(CopilotCompletionService.class);
	}

	public boolean isAvailable(Editor var1);

	public EditorRequest createRequest(Editor var1, int var2, CompletionType var3);

	@RequiresBackgroundThread
	public boolean fetchCompletions(EditorRequest var1, GitHubCopilotToken var2, Integer var3, boolean var4,
			boolean var5, Flow.Subscriber<List<CopilotInlayList>> var6);

	@RequiresBackgroundThread
	default public boolean fetchCompletions(EditorRequest request, GitHubCopilotToken proxyToken,
			Integer maxCompletions, Flow.Subscriber<List<CopilotInlayList>> subscriber) {
		if (request == null) {
			throw new IllegalStateException("request cannot be null!");
		}
		if (subscriber == null) {
			throw new IllegalStateException("subscriber cannot be null!");
		}
		boolean enableCaching = !CopilotApplicationSettings.settings().internalDisableHttpCache;
		return this.fetchCompletions(request, proxyToken, maxCompletions, enableCaching, false, subscriber);
	}

	@RequiresEdt
	public List<CopilotInlayList> fetchCachedCompletions(EditorRequest var1);

	public void reset();

	@RequiresEdt
	public boolean isSupportingOnDemandCycling(Editor var1);

	public boolean isCyclingReplacingCompletions();

	public void sendShownTelemetry(CopilotCompletion var1);

	public void sendAcceptedTelemetry(CopilotCompletion var1, CompletionType var2);

	public void sendRejectedTelemetry(List<CopilotCompletion> var1);

}
