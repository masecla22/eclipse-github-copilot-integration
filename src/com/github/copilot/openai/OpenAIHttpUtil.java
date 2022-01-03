/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.util.text.StringUtilRt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.openai;

import com.intellij.openapi.util.text.StringUtilRt;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class OpenAIHttpUtil {
    private static final String HEADER_COPILOT_EXPERIMENT = "X-Copilot-Experiment";
    private static final String HEADER_REQUEST_ID = "x-request-id";
    private static final String HEADER_COPILOT_PROXY_ROLE = "X-Copilot-Proxy-Role";
    private static final String HEADER_COPILOT_MODEL_ENDPOINT = "X-Copilot-Model-Endpoint";

    OpenAIHttpUtil() {
    }

        static String getRequestId(HttpResponse.ResponseInfo response) {
        return OpenAIHttpUtil.getHeaderOrEmptyString(response, HEADER_REQUEST_ID);
    }

        static String getRequestId(HttpHeaders headers) {
        return OpenAIHttpUtil.getHeaderOrEmptyString(headers, HEADER_REQUEST_ID);
    }

        static String getServerExperiments(HttpResponse.ResponseInfo response) {
        return OpenAIHttpUtil.getHeaderOrEmptyString(response, HEADER_COPILOT_EXPERIMENT);
    }

        static String getServerExperiments(HttpHeaders headers) {
        return OpenAIHttpUtil.getHeaderOrEmptyString(headers, HEADER_COPILOT_EXPERIMENT);
    }

        public static String getProxyRole(HttpResponse.ResponseInfo response) {
        return OpenAIHttpUtil.getHeaderOrEmptyString(response, HEADER_COPILOT_PROXY_ROLE);
    }

        public static String getProxyRole(HttpHeaders headers) {
        return OpenAIHttpUtil.getHeaderOrEmptyString(headers, HEADER_COPILOT_PROXY_ROLE);
    }

        public static String getModelEndpoint(HttpResponse.ResponseInfo response) {
        return OpenAIHttpUtil.getHeaderOrEmptyString(response, HEADER_COPILOT_MODEL_ENDPOINT);
    }

        public static String getModelEndpoint(HttpHeaders headers) {
        return OpenAIHttpUtil.getHeaderOrEmptyString(headers, HEADER_COPILOT_MODEL_ENDPOINT);
    }

        private static String getHeaderOrEmptyString(HttpResponse.ResponseInfo response, String name) {
        if (name == null) {
            throw new IllegalStateException("name cannot be null!");
        }
        if (response == null) {
            return "";
        }
        return OpenAIHttpUtil.getHeaderOrEmptyString(response.headers(), name);
    }

        private static String getHeaderOrEmptyString(HttpHeaders headers, String name) {
        if (name == null) {
            throw new IllegalStateException("name cannot be null!");
        }
        if (headers == null) {
            return "";
        }
        String string = headers.firstValue(name).orElse("");
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

    public static long getProcessingTime(HttpResponse.ResponseInfo response) {
        if (response == null) {
            return -1L;
        }
        Optional<String> timeStr = response.headers().firstValue("openai-processing-ms");
        return (long)StringUtilRt.parseDouble((String)timeStr.orElse(null), (double)-1.0);
    }

    public static boolean isErrorException(Throwable ex) {
        Throwable cause;
        if (ex == null) {
            return false;
        }
        if (ex instanceof CancellationException) {
            return false;
        }
        return !(ex instanceof CompletionException) || !((cause = ex.getCause()) instanceof IOException) || cause.getMessage() == null || !cause.getMessage().endsWith(" cancelled");
    }

    
}

