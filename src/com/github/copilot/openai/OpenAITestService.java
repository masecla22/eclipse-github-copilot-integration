/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.openai;

import com.github.copilot.lang.LanguageSupport;
import com.github.copilot.openai.APIChoice;
import com.github.copilot.openai.OpenAIService;
import com.github.copilot.openai.OpenAIServiceImpl;
import com.github.copilot.request.BlockMode;
import com.github.copilot.request.LanguageEditorRequest;
import com.github.copilot.telemetry.TelemetryData;
import com.intellij.openapi.application.ApplicationManager;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;

public class OpenAITestService
extends OpenAIServiceImpl {
    private final AtomicInteger completionRequestCount = new AtomicInteger();
    public volatile String TEST_COMPLETION_URL;

        public static OpenAITestService getInstance() {
        OpenAITestService openAITestService = (OpenAITestService)ApplicationManager.getApplication().getService(OpenAIService.class);
        if (openAITestService == null) {
            throw new IllegalStateException("openAITestService cannot be null!");
        }
        return openAITestService;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void fetchCompletions(String apiToken, LanguageEditorRequest request, String prompt, int completionCount, double temperature, int maxTokens, int topP, BlockMode blockMode, boolean multilineCompletions, TelemetryData telemetryBaseData,  Flow.Subscriber<APIChoice> subscriber) {
        void subscriber2;
        if (apiToken == null) {
            throw new IllegalStateException("apiToken cannot be null!");
        }
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
        }
        if (prompt == null) {
            throw new IllegalStateException("prompt cannot be null!");
        }
        if (blockMode == null) {
            throw new IllegalStateException("blockMode cannot be null!");
        }
        if (telemetryBaseData == null) {
            throw new IllegalStateException("telemetryBaseData cannot be null!");
        }
        if (subscriber == null) {
            throw new IllegalStateException("subscriber cannot be null!");
        }
        this.completionRequestCount.incrementAndGet();
        super.fetchCompletions(apiToken, request, prompt, completionCount, temperature, maxTokens, topP, blockMode, multilineCompletions, telemetryBaseData, (Flow.Subscriber<APIChoice>)subscriber2);
    }

    @Override
        protected String getCompletionURL(LanguageSupport lang) {
        String override;
        if (lang == null) {
            throw new IllegalStateException("lang cannot be null!");
        }
        if ((override = this.TEST_COMPLETION_URL) != null) {
            String string = override;
            if (string == null) {
                throw new IllegalStateException("string cannot be null!");
            }
            return string;
        }
        String string = super.getCompletionURL(lang);
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

    public int getCompletionRequestCount() {
        return this.completionRequestCount.get();
    }

    public void reset() {
        this.completionRequestCount.set(0);
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "method %s.%s must not return null";
                break;
            }
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/openai/OpenAITestService";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "apiToken";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "request";
                break;
            }
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "prompt";
                break;
            }
            case 4: {
                objectArray2 = objectArray3;
                objectArray3[0] = "blockMode";
                break;
            }
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "telemetryBaseData";
                break;
            }
            case 6: {
                objectArray2 = objectArray3;
                objectArray3[0] = "subscriber";
                break;
            }
            case 7: {
                objectArray2 = objectArray3;
                objectArray3[0] = "lang";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getInstance";
                break;
            }
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/openai/OpenAITestService";
                break;
            }
            case 8: 
            case 9: {
                objectArray = objectArray2;
                objectArray2[1] = "getCompletionURL";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: {
                objectArray = objectArray;
                objectArray[2] = "fetchCompletions";
                break;
            }
            case 7: {
                objectArray = objectArray;
                objectArray[2] = "getCompletionURL";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

