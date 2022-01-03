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

final class OpenAIBodyHandler
implements HttpResponse.BodyHandler<Object> {
    private static final Logger LOG = Logger.getInstance(OpenAIBodyHandler.class);
        private final EditorRequest editorRequest;
        private final JsonToApiChoiceProcessor jsonSubscriber;
    private final Object lock;
    @GuardedBy(value="lock")
    private ServerSideEventProcessor jsonStreamProcessor;

    OpenAIBodyHandler(EditorRequest editorRequest, JsonToApiChoiceProcessor jsonSubscriber) {
        if (editorRequest == null) {
            throw new IllegalStateException("editorRequest cannot be null!");
        }
        if (jsonSubscriber == null) {
            throw new IllegalStateException("jsonSubscriber cannot be null!");
        }
        this.lock = new Object();
        this.editorRequest = editorRequest;
        this.jsonSubscriber = jsonSubscriber;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
        public HttpResponse.BodySubscriber<Object> apply(HttpResponse.ResponseInfo responseInfo) {
        if (responseInfo == null) {
            throw new IllegalStateException("responseInfo cannot be null!");
        }
        this.jsonSubscriber.updateWithResponse(responseInfo);
        int statusCode = responseInfo.statusCode();
        if (statusCode == 466) {
            this.jsonSubscriber.onError(new IncompatibleCopilotClientException(statusCode));
            HttpResponse.BodySubscriber<Object> bodySubscriber = HttpResponse.BodySubscribers.replacing(null);
            if (bodySubscriber == null) {
                throw new IllegalStateException("bodySubscriber cannot be null!");
            }
            return bodySubscriber;
        }
        if (statusCode != 200) {
            this.jsonSubscriber.onComplete();
            HttpResponse.BodySubscriber<Object> bodySubscriber = HttpResponse.BodySubscribers.replacing(null);
            if (bodySubscriber == null) {
                throw new IllegalStateException("bodySubscriber cannot be null!");
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
                throw new IllegalStateException("bodySubscriber cannot be null!");
            }
            return bodySubscriber;
        }
        HttpResponse.BodySubscriber<Object> bodySubscriber = HttpResponse.BodySubscribers.fromLineSubscriber(jsonStreamProcessor, subscriber -> {
            LOG.debug(String.format("%d: lineSubscriber: finished", this.editorRequest.getRequestId()));
            jsonStreamProcessor.close();
            return null;
        }, StandardCharsets.UTF_8, "\n\n");
        if (bodySubscriber == null) {
            throw new IllegalStateException("bodySubscriber cannot be null!");
        }
        return bodySubscriber;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void close(Throwable error) {
        LOG.debug("cancel");
        Object object = this.lock;
        synchronized (object) {
            if (this.jsonStreamProcessor != null) {
                this.jsonStreamProcessor.close(error);
                this.jsonStreamProcessor = null;
            }
        }
    }

    
}

