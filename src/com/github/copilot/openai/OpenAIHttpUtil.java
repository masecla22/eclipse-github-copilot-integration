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

    @NotNull
    static String getRequestId(@Nullable HttpResponse.ResponseInfo response) {
        return OpenAIHttpUtil.getHeaderOrEmptyString(response, HEADER_REQUEST_ID);
    }

    @NotNull
    static String getRequestId(@Nullable HttpHeaders headers) {
        return OpenAIHttpUtil.getHeaderOrEmptyString(headers, HEADER_REQUEST_ID);
    }

    @NotNull
    static String getServerExperiments(@Nullable HttpResponse.ResponseInfo response) {
        return OpenAIHttpUtil.getHeaderOrEmptyString(response, HEADER_COPILOT_EXPERIMENT);
    }

    @NotNull
    static String getServerExperiments(@Nullable HttpHeaders headers) {
        return OpenAIHttpUtil.getHeaderOrEmptyString(headers, HEADER_COPILOT_EXPERIMENT);
    }

    @NotNull
    public static String getProxyRole(@Nullable HttpResponse.ResponseInfo response) {
        return OpenAIHttpUtil.getHeaderOrEmptyString(response, HEADER_COPILOT_PROXY_ROLE);
    }

    @NotNull
    public static String getProxyRole(@Nullable HttpHeaders headers) {
        return OpenAIHttpUtil.getHeaderOrEmptyString(headers, HEADER_COPILOT_PROXY_ROLE);
    }

    @NotNull
    public static String getModelEndpoint(@Nullable HttpResponse.ResponseInfo response) {
        return OpenAIHttpUtil.getHeaderOrEmptyString(response, HEADER_COPILOT_MODEL_ENDPOINT);
    }

    @NotNull
    public static String getModelEndpoint(@Nullable HttpHeaders headers) {
        return OpenAIHttpUtil.getHeaderOrEmptyString(headers, HEADER_COPILOT_MODEL_ENDPOINT);
    }

    @NotNull
    private static String getHeaderOrEmptyString(@Nullable HttpResponse.ResponseInfo response, @NotNull String name) {
        if (name == null) {
            OpenAIHttpUtil.$$$reportNull$$$0(0);
        }
        if (response == null) {
            return "";
        }
        return OpenAIHttpUtil.getHeaderOrEmptyString(response.headers(), name);
    }

    @NotNull
    private static String getHeaderOrEmptyString(@Nullable HttpHeaders headers, @NotNull String name) {
        if (name == null) {
            OpenAIHttpUtil.$$$reportNull$$$0(1);
        }
        if (headers == null) {
            return "";
        }
        String string = headers.firstValue(name).orElse("");
        if (string == null) {
            OpenAIHttpUtil.$$$reportNull$$$0(2);
        }
        return string;
    }

    public static long getProcessingTime(@Nullable HttpResponse.ResponseInfo response) {
        if (response == null) {
            return -1L;
        }
        Optional<String> timeStr = response.headers().firstValue("openai-processing-ms");
        return (long)StringUtilRt.parseDouble((String)timeStr.orElse(null), (double)-1.0);
    }

    public static boolean isErrorException(@Nullable Throwable ex) {
        Throwable cause;
        if (ex == null) {
            return false;
        }
        if (ex instanceof CancellationException) {
            return false;
        }
        return !(ex instanceof CompletionException) || !((cause = ex.getCause()) instanceof IOException) || cause.getMessage() == null || !cause.getMessage().endsWith(" cancelled");
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
            case 2: {
                string = "@NotNull method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 2: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "name";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/openai/OpenAIHttpUtil";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/openai/OpenAIHttpUtil";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "getHeaderOrEmptyString";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "getHeaderOrEmptyString";
                break;
            }
            case 2: {
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 2: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

