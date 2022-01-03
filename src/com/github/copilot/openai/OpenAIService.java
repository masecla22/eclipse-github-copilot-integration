/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.openai;

import com.github.copilot.openai.APIChoice;
import com.github.copilot.request.BlockMode;
import com.github.copilot.request.LanguageEditorRequest;
import com.github.copilot.telemetry.TelemetryData;
import com.intellij.openapi.application.ApplicationManager;
import java.util.concurrent.Flow;
import org.jetbrains.annotations.NotNull;

public interface OpenAIService {
    @NotNull
    public static OpenAIService getInstance() {
        OpenAIService openAIService = (OpenAIService)ApplicationManager.getApplication().getService(OpenAIService.class);
        if (openAIService == null) {
            OpenAIService.$$$reportNull$$$0(0);
        }
        return openAIService;
    }

    public void fetchCompletions(@NotNull String var1, @NotNull LanguageEditorRequest var2, @NotNull String var3, int var4, double var5, int var7, int var8, @NotNull BlockMode var9, boolean var10, @NotNull TelemetryData var11, @NotNull Flow.Subscriber<APIChoice> var12);

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "com/github/copilot/openai/OpenAIService", "getInstance"));
    }
}

