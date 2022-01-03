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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CopilotCompletionService {
    public static CopilotCompletionService getInstance() {
        return (CopilotCompletionService)ApplicationManager.getApplication().getService(CopilotCompletionService.class);
    }

    public boolean isAvailable(@NotNull Editor var1);

    @Nullable
    public EditorRequest createRequest(@NotNull Editor var1, int var2, @NotNull CompletionType var3);

    @RequiresBackgroundThread
    public boolean fetchCompletions(@NotNull EditorRequest var1, @Nullable GitHubCopilotToken var2, @Nullable Integer var3, boolean var4, boolean var5, @NotNull Flow.Subscriber<List<CopilotInlayList>> var6);

    @RequiresBackgroundThread
    default public boolean fetchCompletions(@NotNull EditorRequest request, @Nullable GitHubCopilotToken proxyToken, @Nullable Integer maxCompletions, @NotNull Flow.Subscriber<List<CopilotInlayList>> subscriber) {
        if (request == null) {
            CopilotCompletionService.$$$reportNull$$$0(0);
        }
        if (subscriber == null) {
            CopilotCompletionService.$$$reportNull$$$0(1);
        }
        boolean enableCaching = !CopilotApplicationSettings.settings().internalDisableHttpCache;
        return this.fetchCompletions(request, proxyToken, maxCompletions, enableCaching, false, subscriber);
    }

    @RequiresEdt
    @Nullable
    public List<CopilotInlayList> fetchCachedCompletions(@NotNull EditorRequest var1);

    public void reset();

    @RequiresEdt
    public boolean isSupportingOnDemandCycling(@NotNull Editor var1);

    public boolean isCyclingReplacingCompletions();

    public void sendShownTelemetry(@NotNull CopilotCompletion var1);

    public void sendAcceptedTelemetry(@NotNull CopilotCompletion var1, @NotNull CompletionType var2);

    public void sendRejectedTelemetry(@NotNull List<CopilotCompletion> var1);

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[0] = "request";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[0] = "subscriber";
                break;
            }
        }
        objectArray[1] = "com/github/copilot/completions/CopilotCompletionService";
        objectArray[2] = "fetchCompletions";
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objectArray));
    }
}

