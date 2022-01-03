/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.util.Disposer
 *  it.unimi.dsi.fastutil.objects.Object2DoubleMap
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.openai;

import com.github.copilot.CopilotPlugin;
import com.github.copilot.lang.LanguageSupport;
import com.github.copilot.openai.APIChoice;
import com.github.copilot.openai.JsonToApiChoiceProcessor;
import com.github.copilot.openai.OpenAIBodyHandler;
import com.github.copilot.openai.OpenAIHttpUtil;
import com.github.copilot.openai.OpenAIService;
import com.github.copilot.openai.OpenAiRequestBody;
import com.github.copilot.openai.StringDoublePair;
import com.github.copilot.request.BlockMode;
import com.github.copilot.request.EditorRequest;
import com.github.copilot.request.LanguageEditorRequest;
import com.github.copilot.telemetry.TelemetryData;
import com.github.copilot.telemetry.TelemetryService;
import com.github.copilot.util.String2DoubleMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Disposer;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpenAIServiceImpl
implements OpenAIService {
    static final Gson GSON = new GsonBuilder().registerTypeAdapter(Object2DoubleMap.class, (Object)new String2DoubleMap.TypeAdapter()).registerTypeAdapter(StringDoublePair.class, (Object)new StringDoublePair.TypeAdapter()).disableHtmlEscaping().create();
    private static final Logger LOG = Logger.getInstance(OpenAIServiceImpl.class);
    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(10000L)).followRedirects(HttpClient.Redirect.NEVER).version(HttpClient.Version.HTTP_2).build();
    private final String[] staticHttpHeaders = new String[]{"Content-Type", "application/json", "User-Agent", "IntelliJCopilot/" + CopilotPlugin.getVersion(), "Openai-Organization", "github-copilot", "Editor-Version", CopilotPlugin.editorVersionString(), "Editor-Plugin-Version", CopilotPlugin.pluginVersionString()};

    /*
     * WARNING - void declaration
     */
    @Override
    public void fetchCompletions(String apiToken, LanguageEditorRequest request, String prompt, int completionCount, double temperature, int maxTokens, int topP, BlockMode blockMode, boolean multilineCompletions, TelemetryData telemetryBaseData, Flow.Subscriber<APIChoice> subscriber) {
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
        LanguageSupport lang = request.getLanguage();
        String url = this.getCompletionURL(lang);
        TelemetryData telemetryData = TelemetryData.create(Map.of("endpoint", "https://copilot-proxy.githubusercontent.com", "engineName", lang.getCopilotLanguage().getEngineName(), "uiKind", request.getCompletionType().getTelemetryPropertyValue()), String2DoubleMap.of("promptCharLen", prompt.length()));
        TelemetryService.getInstance().track("request.sent", telemetryData);
        String body = this.createCompletionBody(prompt, completionCount, temperature, maxTokens, topP, multilineCompletions ? lang.getMultiLineStops() : lang.getSingleLineStops(), blockMode, Math.max(0, request.getLineInfo().getNextLineIndent()));
        if (LOG.isTraceEnabled()) {
            LOG.trace("Sending completions request: " + body);
        }
        JsonToApiChoiceProcessor processor = new JsonToApiChoiceProcessor(request, telemetryBaseData, (Flow.Subscriber<APIChoice>)subscriber2);
        Disposer.tryRegister((Disposable)request.getDisposable(), processor::cancel);
        CompletableFuture<HttpResponse<Object>> response = this.postStreaming(url, apiToken, body, request, processor);
        response.whenComplete((httpResponse, ex) -> {
            long totalRequestTime = System.currentTimeMillis() - request.getRequestTimestamp();
            if (httpResponse != null) {
                String requestId = OpenAIHttpUtil.getRequestId(httpResponse.headers());
                TelemetryData responseTelemetry = TelemetryData.extend(telemetryData, Map.of("headerRequestId", requestId, "serverExperiments", OpenAIHttpUtil.getServerExperiments(httpResponse.headers())), String2DoubleMap.of("totalTimeMs", totalRequestTime));
                TelemetryService.getInstance().track("request.response", responseTelemetry);
                TelemetryService.getInstance().trackSecure("engine.prompt", Map.of("prompt", prompt, "headerRequestId", requestId));
            } else if (OpenAIHttpUtil.isErrorException(ex)) {
                TelemetryService.getInstance().track("request.error", TelemetryData.extend(telemetryData, Map.of("message", ex.getMessage()), String2DoubleMap.of("totalTimeMs", totalRequestTime)));
            }
        });
    }

        protected String getCompletionURL(LanguageSupport lang) {
        String string = "https://copilot-proxy.githubusercontent.com" + lang.getCopilotLanguage().getEnginePath() + "/completions";
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

    private CompletableFuture<HttpResponse<Object>> postStreaming(String url, String secretKey, String requestBody, EditorRequest editorRequest, JsonToApiChoiceProcessor jsonProcessor) {
        if (url == null) {
            throw new IllegalStateException("url cannot be null!");
        }
        if (secretKey == null) {
            throw new IllegalStateException("secretKey cannot be null!");
        }
        if (editorRequest == null) {
            throw new IllegalStateException("editorRequest cannot be null!");
        }
        if (jsonProcessor == null) {
            throw new IllegalStateException("jsonProcessor cannot be null!");
        }
        LOG.debug(String.format("%d: New HTTP request", editorRequest.getRequestId()));
        long start = System.currentTimeMillis();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(url)).timeout(Duration.ofMillis(20000L)).header("Authorization", "Bearer " + secretKey).headers(this.staticHttpHeaders);
        String intent = editorRequest.getCompletionType().getOpenAiIntent();
        if (intent != null) {
            requestBuilder.setHeader("OpenAI-Intent", intent);
        }
        HttpRequest request = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBody == null ? "" : requestBody, StandardCharsets.UTF_8)).build();
        OpenAIBodyHandler bodyHandler = new OpenAIBodyHandler(editorRequest, jsonProcessor);
        CompletableFuture<HttpResponse<Object>> response = this.httpClient.sendAsync(request, bodyHandler);
        Disposer.tryRegister((Disposable)editorRequest.getDisposable(), () -> response.cancel(true));
        response.whenComplete((resp, ex) -> {
            if (OpenAIHttpUtil.isErrorException(ex)) {
                LOG.debug("HTTP response completed with exception: " + ex.getMessage());
                if (LOG.isTraceEnabled()) {
                    LOG.trace(ex);
                }
                bodyHandler.close((Throwable)ex);
            } else {
                bodyHandler.close(null);
            }
            if (LOG.isTraceEnabled()) {
                LOG.trace(String.format("%d: HTTP completion request: %d ms, ex: %s, resp: %d", editorRequest.getRequestId(), System.currentTimeMillis() - start, ex, resp != null ? resp.statusCode() : -1));
            }
        });
        return response;
    }

    /*
     * WARNING - void declaration
     */
        private String createCompletionBody(String prompt, int count, double temperature, int maxTokens, int topP, String[] stopMarkers, BlockMode blockMode, int n) {
        void nextLineIndent;
        if (prompt == null) {
            throw new IllegalStateException("prompt cannot be null!");
        }
        if (blockMode == null) {
            throw new IllegalStateException("blockMode cannot be null!");
        }
        if (stopMarkers == null) {
            throw new IllegalStateException("stopMarkers cannot be null!");
        }
        OpenAiRequestBody request = new OpenAiRequestBody(prompt, maxTokens, temperature, topP, count, 2, true, stopMarkers, blockMode == BlockMode.ServerSideIndentation, (int)nextLineIndent);
        String string = GSON.toJson((Object)request);
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
            case 6: 
            case 14: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 6: 
            case 14: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "apiToken";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "request";
                break;
            }
            case 2: 
            case 11: {
                objectArray2 = objectArray3;
                objectArray3[0] = "prompt";
                break;
            }
            case 3: 
            case 12: {
                objectArray2 = objectArray3;
                objectArray3[0] = "blockMode";
                break;
            }
            case 4: {
                objectArray2 = objectArray3;
                objectArray3[0] = "telemetryBaseData";
                break;
            }
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "subscriber";
                break;
            }
            case 6: 
            case 14: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/openai/OpenAIServiceImpl";
                break;
            }
            case 7: {
                objectArray2 = objectArray3;
                objectArray3[0] = "url";
                break;
            }
            case 8: {
                objectArray2 = objectArray3;
                objectArray3[0] = "secretKey";
                break;
            }
            case 9: {
                objectArray2 = objectArray3;
                objectArray3[0] = "editorRequest";
                break;
            }
            case 10: {
                objectArray2 = objectArray3;
                objectArray3[0] = "jsonProcessor";
                break;
            }
            case 13: {
                objectArray2 = objectArray3;
                objectArray3[0] = "stopMarkers";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/openai/OpenAIServiceImpl";
                break;
            }
            case 6: {
                objectArray = objectArray2;
                objectArray2[1] = "getCompletionURL";
                break;
            }
            case 14: {
                objectArray = objectArray2;
                objectArray2[1] = "createCompletionBody";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "fetchCompletions";
                break;
            }
            case 6: 
            case 14: {
                break;
            }
            case 7: 
            case 8: 
            case 9: 
            case 10: {
                objectArray = objectArray;
                objectArray[2] = "postStreaming";
                break;
            }
            case 11: 
            case 12: 
            case 13: {
                objectArray = objectArray;
                objectArray[2] = "createCompletionBody";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 6: 
            case 14: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

