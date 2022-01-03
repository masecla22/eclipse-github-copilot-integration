/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.util.ThrowableRunnable
 *  com.intellij.util.messages.Topic
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.TestOnly
 */
package com.github.copilot.completions;

import com.github.copilot.completions.CompletionUtil;
import com.github.copilot.completions.CopilotCompletion;
import com.github.copilot.completions.CopilotCompletionService;
import com.github.copilot.completions.CopilotInlayList;
import com.github.copilot.github.GitHubCopilotToken;
import com.github.copilot.lang.agent.DelegatingCompletionService;
import com.github.copilot.request.EditorRequest;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.ThrowableRunnable;
import com.intellij.util.messages.Topic;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.TestOnly;

@TestOnly
public class CopilotCompletionTestService
extends DelegatingCompletionService {
    private static final Topic<RequestNotification> REQUEST_TOPIC = Topic.create((String)"copilot.completions", RequestNotification.class);
    private final AtomicInteger requestCounter = new AtomicInteger();
        private volatile List<CopilotCompletion> mockCompletions = null;

        public static CopilotCompletionTestService getInstance() {
        CopilotCompletionTestService copilotCompletionTestService = (CopilotCompletionTestService)ApplicationManager.getApplication().getService(CopilotCompletionService.class);
        if (copilotCompletionTestService == null) {
            throw new IllegalStateException("copilotCompletionTestService cannot be null!");
        }
        return copilotCompletionTestService;
    }

    public void withMockCompletions(List<CopilotCompletion> completions, ThrowableRunnable<Exception> action) throws Exception {
        if (completions == null) {
            throw new IllegalStateException("completions cannot be null!");
        }
        if (action == null) {
            throw new IllegalStateException("action cannot be null!");
        }
        try {
            this.mockCompletions = completions;
            action.run();
        }
        finally {
            this.mockCompletions = null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean fetchCompletions(EditorRequest request, GitHubCopilotToken token, Integer maxCompletions, boolean enableCaching, boolean cycling, Flow.Subscriber<List<CopilotInlayList>> subscriber) {
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
        }
        if (subscriber == null) {
            throw new IllegalStateException("subscriber cannot be null!");
        }
        this.requestCounter.incrementAndGet();
        List<CopilotCompletion> mockCompletions = this.mockCompletions;
        if (mockCompletions != null) {
            try (SubmissionPublisher<List<CopilotInlayList>> publisher = new SubmissionPublisher<List<CopilotInlayList>>();){
                publisher.subscribe(subscriber);
                publisher.submit(CompletionUtil.createEditorCompletions(request, mockCompletions));
            }
            finally {
                ((RequestNotification)ApplicationManager.getApplication().getMessageBus().syncPublisher(REQUEST_TOPIC)).completionRequest();
            }
            return true;
        }
        return super.fetchCompletions(request, token, maxCompletions, enableCaching, cycling, subscriber);
    }

    @Override
        public List<CopilotInlayList> fetchCachedCompletions(EditorRequest request) {
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
        }
        return super.fetchCachedCompletions(request);
    }

    public int getRequestCount() {
        return this.requestCounter.get();
    }

    @Override
    public void reset() {
        super.reset();
        this.mockCompletions = null;
        this.requestCounter.set(0);
    }

    public RequestLatch newCompletionLatch(int expectedRequests, Disposable parent) {
        if (parent == null) {
            throw new IllegalStateException("parent cannot be null!");
        }
        return new RequestLatch(expectedRequests, parent);
    }

    

    public static class RequestLatch {
        private volatile CountDownLatch latch;

        public RequestLatch(int expectedRequests, Disposable parent) {
            if (parent == null) {
                throw new IllegalStateException("parent cannot be null!");
            }
            this.latch = new CountDownLatch(expectedRequests);
            ApplicationManager.getApplication().getMessageBus().connect(parent).subscribe(REQUEST_TOPIC, () -> ApplicationManager.getApplication().executeOnPooledThread(() -> this.latch.countDown()));
        }

        public void reset(int expectedRequests) {
            this.latch = new CountDownLatch(expectedRequests);
        }

        public void await() throws InterruptedException {
            this.latch.await(30L, TimeUnit.SECONDS);
        }
    }

    static interface RequestNotification {
        public void completionRequest();
    }
}

