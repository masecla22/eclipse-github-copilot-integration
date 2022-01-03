/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonSyntaxException
 *  com.intellij.openapi.diagnostic.Logger
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.openai;

import com.github.copilot.CopilotExecutors;
import com.github.copilot.openai.OpenAI;
import com.github.copilot.request.EditorRequest;
import com.github.copilot.util.RequestCancelledException;
import com.github.copilot.util.RequestTimeoutException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.intellij.openapi.diagnostic.Logger;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ServerSideEventProcessor
implements Flow.Subscriber<String> {
    private static final Logger LOG = Logger.getInstance(ServerSideEventProcessor.class);
    private final EditorRequest request;
    private final SubmissionPublisher<JsonObject> eventPublisher;
    private final AtomicBoolean isClosed;
    private final AtomicBoolean receivedDone;
    private volatile Flow.Subscription subscription;

    ServerSideEventProcessor(EditorRequest request, Flow.Subscriber<JsonObject> jsonSubscriber) {
        if (request == null) {
            ServerSideEventProcessor.$$$reportNull$$$0(0);
        }
        if (jsonSubscriber == null) {
            ServerSideEventProcessor.$$$reportNull$$$0(1);
        }
        this.isClosed = new AtomicBoolean();
        this.receivedDone = new AtomicBoolean();
        this.request = request;
        this.eventPublisher = new SubmissionPublisher(CopilotExecutors.getExecutor(), Flow.defaultBufferSize());
        this.eventPublisher.subscribe(jsonSubscriber);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        if (subscription == null) {
            ServerSideEventProcessor.$$$reportNull$$$0(2);
        }
        assert (this.subscription == null) : "only a single subscription is supported";
        this.subscription = subscription;
        this.subscription.request(1L);
    }

    @Override
    public void onNext(String item) {
        if (item == null) {
            ServerSideEventProcessor.$$$reportNull$$$0(3);
        }
        if (this.isClosed.get() || this.request.isCancelled()) {
            LOG.debug(String.format("%d: onNext already closed", this.request.getRequestId()));
            return;
        }
        if ("data: [DONE]".equals(item)) {
            LOG.debug(String.format("%d: received [DONE] marker", this.request.getRequestId()));
            this.receivedDone.set(true);
            this.close();
            return;
        }
        if (!item.startsWith("data:")) {
            LOG.debug("unsupported server-side-event: " + item);
            if (item.contains("\"error\"") && item.contains("request cancelled")) {
                this.close(new RequestCancelledException("editor request was cancelled on the server"));
            } else if (item.contains("\"error\"") && item.contains("request timeout")) {
                this.close(new RequestTimeoutException("server-side request timeout: " + item));
            } else {
                this.close(new IllegalStateException("unsupported server-side-event: " + item));
            }
            return;
        }
        try {
            String line = item.substring(5).trim();
            JsonObject json = (JsonObject)OpenAI.GSON.fromJson(line, JsonObject.class);
            if (LOG.isTraceEnabled()) {
                LOG.trace(String.format("%d: publish JSON object", this.request.getRequestId()));
            }
            this.subscription.request(1L);
            this.eventPublisher.submit(json);
        }
        catch (JsonSyntaxException e) {
            LOG.warn("JSON syntax exception: " + item, (Throwable)e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("onError: " + throwable.getMessage());
        }
        this.eventPublisher.closeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        if (!this.receivedDone.get()) {
            LOG.warn("server did not send [DONE] marker");
        }
        this.eventPublisher.close();
    }

    void close() {
        this.close(null);
    }

    void close(Throwable error) {
        LOG.debug("close()");
        this.isClosed.set(true);
        if (error != null) {
            this.eventPublisher.closeExceptionally(error);
        } else {
            this.eventPublisher.close();
        }
        if (this.subscription != null) {
            this.subscription.cancel();
            this.subscription = null;
        }
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2;
        Object[] objectArray3 = new Object[3];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "request";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "jsonSubscriber";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "subscription";
                break;
            }
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "item";
                break;
            }
        }
        objectArray2[1] = "com/github/copilot/openai/ServerSideEventProcessor";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "<init>";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[2] = "onSubscribe";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[2] = "onNext";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

