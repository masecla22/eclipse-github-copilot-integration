/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.util.Disposer
 *  javax.annotation.concurrent.GuardedBy
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.openai;

import com.github.copilot.openai.IncompatibleCopilotClientException;
import com.github.copilot.openai.JsonToApiChoiceProcessor;
import com.github.copilot.openai.ServerSideEventProcessor;
import com.github.copilot.request.EditorRequest;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Disposer;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import javax.annotation.concurrent.GuardedBy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class OpenAIBodyHandler
implements HttpResponse.BodyHandler<Object> {
    private static final Logger LOG = Logger.getInstance(OpenAIBodyHandler.class);
    @NotNull
    private final EditorRequest editorRequest;
    @NotNull
    private final JsonToApiChoiceProcessor jsonSubscriber;
    private final Object lock;
    @GuardedBy(value="lock")
    private ServerSideEventProcessor jsonStreamProcessor;

    OpenAIBodyHandler(@NotNull EditorRequest editorRequest, @NotNull JsonToApiChoiceProcessor jsonSubscriber) {
        if (editorRequest == null) {
            OpenAIBodyHandler.$$$reportNull$$$0(0);
        }
        if (jsonSubscriber == null) {
            OpenAIBodyHandler.$$$reportNull$$$0(1);
        }
        this.lock = new Object();
        this.editorRequest = editorRequest;
        this.jsonSubscriber = jsonSubscriber;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    @NotNull
    public HttpResponse.BodySubscriber<Object> apply(@NotNull HttpResponse.ResponseInfo responseInfo) {
        if (responseInfo == null) {
            OpenAIBodyHandler.$$$reportNull$$$0(2);
        }
        this.jsonSubscriber.updateWithResponse(responseInfo);
        int statusCode = responseInfo.statusCode();
        if (statusCode == 466) {
            this.jsonSubscriber.onError(new IncompatibleCopilotClientException(statusCode));
            HttpResponse.BodySubscriber<Object> bodySubscriber = HttpResponse.BodySubscribers.replacing(null);
            if (bodySubscriber == null) {
                OpenAIBodyHandler.$$$reportNull$$$0(3);
            }
            return bodySubscriber;
        }
        if (statusCode != 200) {
            this.jsonSubscriber.onComplete();
            HttpResponse.BodySubscriber<Object> bodySubscriber = HttpResponse.BodySubscribers.replacing(null);
            if (bodySubscriber == null) {
                OpenAIBodyHandler.$$$reportNull$$$0(4);
            }
            return bodySubscriber;
        }
        ServerSideEventProcessor jsonStreamProcessor = new ServerSideEventProcessor(this.editorRequest, this.jsonSubscriber);
        Object object = this.lock;
        synchronized (object) {
            this.jsonStreamProcessor = jsonStreamProcessor;
        }
        if (!Disposer.tryRegister((Disposable)this.editorRequest.getDisposable(), jsonStreamProcessor::close)) {
            jsonStreamProcessor.close();
            HttpResponse.BodySubscriber<Object> bodySubscriber = HttpResponse.BodySubscribers.replacing(null);
            if (bodySubscriber == null) {
                OpenAIBodyHandler.$$$reportNull$$$0(5);
            }
            return bodySubscriber;
        }
        HttpResponse.BodySubscriber<Object> bodySubscriber = HttpResponse.BodySubscribers.fromLineSubscriber(jsonStreamProcessor, subscriber -> {
            LOG.debug(String.format("%d: lineSubscriber: finished", this.editorRequest.getRequestId()));
            jsonStreamProcessor.close();
            return null;
        }, StandardCharsets.UTF_8, "\n\n");
        if (bodySubscriber == null) {
            OpenAIBodyHandler.$$$reportNull$$$0(6);
        }
        return bodySubscriber;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void close(@Nullable Throwable error) {
        LOG.debug("cancel");
        Object object = this.lock;
        synchronized (object) {
            if (this.jsonStreamProcessor != null) {
                this.jsonStreamProcessor.close(error);
                this.jsonStreamProcessor = null;
            }
        }
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
            case 3: 
            case 4: 
            case 5: 
            case 6: {
                string = "@NotNull method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 3: 
            case 4: 
            case 5: 
            case 6: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "editorRequest";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "jsonSubscriber";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "responseInfo";
                break;
            }
            case 3: 
            case 4: 
            case 5: 
            case 6: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/openai/OpenAIBodyHandler";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/openai/OpenAIBodyHandler";
                break;
            }
            case 3: 
            case 4: 
            case 5: 
            case 6: {
                objectArray = objectArray2;
                objectArray2[1] = "apply";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "<init>";
                break;
            }
            case 2: {
                objectArray = objectArray;
                objectArray[2] = "apply";
                break;
            }
            case 3: 
            case 4: 
            case 5: 
            case 6: {
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 3: 
            case 4: 
            case 5: 
            case 6: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

