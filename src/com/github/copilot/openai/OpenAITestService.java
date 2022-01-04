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

public class OpenAITestService extends OpenAIServiceImpl {
	private final AtomicInteger completionRequestCount = new AtomicInteger();
	public volatile String TEST_COMPLETION_URL;

	public static OpenAITestService getInstance() {
		OpenAITestService openAITestService = (OpenAITestService) ApplicationManager.getApplication()
				.getService(OpenAIService.class);
		if (openAITestService == null) {
			throw new IllegalStateException("openAITestService cannot be null!");
		}
		return openAITestService;
	}

	/*
	 * WARNING - void declaration
	 */
	@Override
	public void fetchCompletions(String apiToken, LanguageEditorRequest request, String prompt, int completionCount,
			double temperature, int maxTokens, int topP, BlockMode blockMode, boolean multilineCompletions,
			TelemetryData telemetryBaseData, Flow.Subscriber<APIChoice> subscriber) {
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
		super.fetchCompletions(apiToken, request, prompt, completionCount, temperature, maxTokens, topP, blockMode,
				multilineCompletions, telemetryBaseData, (Flow.Subscriber<APIChoice>) subscriber2);
	}

	@Override
	protected String getCompletionURL(LanguageSupport lang) {
		String override;
		if (lang == null) {
			throw new IllegalStateException("lang cannot be null!");
		}
		if ((override = this.TEST_COMPLETION_URL) != null) {
			String string = override;
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

}
