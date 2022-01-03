/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.util.Disposer
 *  it.unimi.dsi.fastutil.objects.Object2DoubleMap
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.completions;

import com.github.copilot.CopilotExecutors;
import com.github.copilot.completions.CompletionUtil;
import com.github.copilot.completions.CopilotInlayList;
import com.github.copilot.lang.prompt.PromptInfo;
import com.github.copilot.openai.APIChoice;
import com.github.copilot.request.EditorRequest;
import com.github.copilot.telemetry.TelemetryData;
import com.github.copilot.telemetry.TelemetryService;
import com.github.copilot.util.String2DoubleMap;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Disposer;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ApiChoiceToInlaySetProcessor
implements Flow.Subscriber<APIChoice> {
    private static final Logger LOG = Logger.getInstance(ApiChoiceToInlaySetProcessor.class);
        private final EditorRequest request;
        private final PromptInfo prompt;
        private final TelemetryData baseTelemetryData;
        private final Consumer<APIChoice> onNewItem;
        private final SubmissionPublisher<List<CopilotInlayList>> publisher;
    private volatile Flow.Subscription subscription;
    private volatile boolean hasFirstItem;

    ApiChoiceToInlaySetProcessor(EditorRequest request, PromptInfo prompt, TelemetryData baseTelemetryData, Flow.Subscriber<List<CopilotInlayList>> subscriber, Consumer<APIChoice> onNewItem) {
        if (request == null) {
            ApiChoiceToInlaySetProcessor.$$$reportNull$$$0(0);
        }
        if (prompt == null) {
            ApiChoiceToInlaySetProcessor.$$$reportNull$$$0(1);
        }
        if (baseTelemetryData == null) {
            ApiChoiceToInlaySetProcessor.$$$reportNull$$$0(2);
        }
        if (subscriber == null) {
            ApiChoiceToInlaySetProcessor.$$$reportNull$$$0(3);
        }
        if (onNewItem == null) {
            ApiChoiceToInlaySetProcessor.$$$reportNull$$$0(4);
        }
        this.request = request;
        this.prompt = prompt;
        this.baseTelemetryData = baseTelemetryData;
        this.onNewItem = onNewItem;
        this.publisher = new SubmissionPublisher(CopilotExecutors.getExecutor(), Flow.defaultBufferSize());
        this.publisher.subscribe(subscriber);
        Disposer.tryRegister((Disposable)request.getDisposable(), () -> {
            this.publisher.close();
            if (this.subscription != null) {
                this.subscription.cancel();
            }
        });
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1L);
    }

    @Override
    public void onNext(APIChoice item) {
        CopilotInlayList inlays;
        if (LOG.isTraceEnabled()) {
            LOG.trace(String.format("%d: completion: publishing inlay for APIChoice", item.getRequestID()));
        }
        if (!this.hasFirstItem) {
            this.hasFirstItem = true;
            ApiChoiceToInlaySetProcessor.trackCompletionPerformance(item, this.baseTelemetryData);
        }
        if (!this.request.isCancelled()) {
            this.subscription.request(1L);
        }
        if ((inlays = CompletionUtil.createEditorCompletion(this.request, item, true)) != null && !inlays.isEmpty()) {
            this.publisher.submit(List.of(inlays));
            try {
                this.onNewItem.accept(item);
            }
            catch (Exception e) {
                LOG.warn("error invoking onNewItem", (Throwable)e);
            }
        }
    }

    @Override
    public void onError(Throwable throwable) {
        LOG.debug("onError");
        this.publisher.closeExceptionally(throwable);
        TelemetryData data = TelemetryData.extend(this.baseTelemetryData, Map.of("cancelledNetworkRequest", "true"), String2DoubleMap.of("timeSinceIssuedMs", System.currentTimeMillis() - this.request.getRequestTimestamp()));
        TelemetryService.getInstance().track("ghostText.canceled", data);
    }

    @Override
    public void onComplete() {
        LOG.debug("onComplete");
        this.publisher.close();
    }

    private static void trackCompletionPerformance(APIChoice item, TelemetryData baseData) {
        if (item == null) {
            ApiChoiceToInlaySetProcessor.$$$reportNull$$$0(5);
        }
        long requestTime = System.currentTimeMillis() - item.getResponseInfo().getRequestStartTimestamp();
        long processingTime = item.getResponseInfo().getProcessingTimeMillis();
        long deltaMS = requestTime - processingTime;
        Double meanLogProb = item.getMeanLogProb();
        HashMap<String, String> props = new HashMap<String, String>();
        props.put("headerRequestId", item.getResponseInfo().getHeaderRequestId());
        props.put("serverExperiments", item.getResponseInfo().getServerExperiments());
        props.put("completionId", item.getCompletionId());
        props.put("created", String.valueOf(item.getCreatedTimestamp()));
        String2DoubleMap metrics = new String2DoubleMap();
        metrics.put("completionCharLen", item.getCompletionTextLength());
        metrics.put("requestTimeMs", requestTime);
        metrics.put("processingTimeMs", processingTime);
        metrics.put("deltaMs", deltaMS);
        metrics.put("meanLogProb", meanLogProb == null ? Double.NaN : meanLogProb);
        metrics.put("numTokens", item.getNumTokens());
        TelemetryService.getInstance().track("ghostText.performance", TelemetryData.extend(baseData, props, (Object2DoubleMap<String>)metrics));
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
                objectArray3[0] = "prompt";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "baseTelemetryData";
                break;
            }
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "subscriber";
                break;
            }
            case 4: {
                objectArray2 = objectArray3;
                objectArray3[0] = "onNewItem";
                break;
            }
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "item";
                break;
            }
        }
        objectArray2[1] = "com/github/copilot/completions/ApiChoiceToInlaySetProcessor";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "<init>";
                break;
            }
            case 5: {
                objectArray = objectArray2;
                objectArray2[2] = "trackCompletionPerformance";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

