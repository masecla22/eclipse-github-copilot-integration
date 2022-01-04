/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.intellij.openapi.diagnostic.Logger
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.openai;

import com.github.copilot.CopilotExecutors;
import com.github.copilot.openai.APIChoice;
import com.github.copilot.openai.APIChoiceTransformer;
import com.github.copilot.request.EditorRequest;
import com.github.copilot.request.LanguageEditorRequest;
import com.github.copilot.telemetry.TelemetryData;
import com.github.copilot.util.Cancellable;
import com.github.copilot.util.RequestCancelledException;
import com.google.gson.JsonObject;
import com.intellij.openapi.diagnostic.Logger;
import java.net.http.HttpResponse;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

class JsonToApiChoiceProcessor implements Flow.Processor<JsonObject, APIChoice>, Cancellable {
	private static final Logger LOG = Logger.getInstance(JsonToApiChoiceProcessor.class);
	private final SubmissionPublisher<APIChoice> apiChoicePublisher;
	private final APIChoiceTransformer choiceTransformer;
	private final EditorRequest request;
	private volatile boolean cancelled;
	private volatile Flow.Subscription subscription;

	JsonToApiChoiceProcessor(LanguageEditorRequest request, TelemetryData telemetryBaseData,
			Flow.Subscriber<APIChoice> subscriber) {
		if (request == null) {
			throw new IllegalStateException("request cannot be null!");
		}
		if (telemetryBaseData == null) {
			throw new IllegalStateException("telemetryBaseData cannot be null!");
		}
		if (subscriber == null) {
			throw new IllegalStateException("subscriber cannot be null!");
		}
		this.request = request;
		this.apiChoicePublisher = new SubmissionPublisher(CopilotExecutors.getExecutor(), Flow.defaultBufferSize());
		this.choiceTransformer = new APIChoiceTransformer(request, telemetryBaseData, this.apiChoicePublisher::submit);
		this.apiChoicePublisher.subscribe(subscriber);
	}

	void updateWithResponse(HttpResponse.ResponseInfo responseInfo) {
		if (responseInfo == null) {
			throw new IllegalStateException("responseInfo cannot be null!");
		}
		this.choiceTransformer.updateWithResponse(responseInfo);
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void cancel() {
		LOG.debug(String.format("%d: APIChoice.cancel", this.request.getRequestId()));
		this.cancelled = true;
		Flow.Subscription subscription = this.subscription;
		if (subscription != null) {
			this.subscription = null;
			subscription.cancel();
			this.apiChoicePublisher.closeExceptionally(new RequestCancelledException("editor request was cancelled"));
		}
	}

	@Override
	public void subscribe(Flow.Subscriber<? super APIChoice> subscriber) {
		this.apiChoicePublisher.subscribe(subscriber);
	}

	@Override
	public void onSubscribe(Flow.Subscription subscription) {
		this.subscription = subscription;
		this.subscription.request(1L);
	}

	@Override
	public void onNext(JsonObject item) {
		LOG.trace(String.format("%d: APIChoice.onNext", this.request.getRequestId()));
		if (this.isCancelled() || this.request.isCancelled()) {
			return;
		}
		if (item.has("error")) {
			LOG.warn("Error in response:" + item.get("error"));
			return;
		}
		if (!this.isCancelled() && !this.request.isCancelled()) {
			this.subscription.request(1L);
		}
		this.choiceTransformer.process(item);
	}

	@Override
	public void onError(Throwable throwable) {
		LOG.debug(String.format("%d: onError: %s", this.request.getRequestId(), throwable));
		this.apiChoicePublisher.closeExceptionally(throwable);
		this.choiceTransformer.close();
		Flow.Subscription subscription = this.subscription;
		if (subscription != null) {
			this.subscription = null;
			subscription.cancel();
		}
	}

	@Override
	public void onComplete() {
		LOG.debug(String.format("%d: onComplete", this.request.getRequestId()));
		this.apiChoicePublisher.close();
		this.choiceTransformer.close();
	}

}
