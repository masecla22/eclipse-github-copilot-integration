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
        public static OpenAIService getInstance() {
        OpenAIService openAIService = (OpenAIService)ApplicationManager.getApplication().getService(OpenAIService.class);
        if (openAIService == null) {
            throw new IllegalStateException("openAIService cannot be null!");
        }
        return openAIService;
    }

    public void fetchCompletions(String var1, LanguageEditorRequest var2, String var3, int var4, double var5, int var7, int var8, BlockMode var9, boolean var10, TelemetryData var11, Flow.Subscriber<APIChoice> var12);

    
}

