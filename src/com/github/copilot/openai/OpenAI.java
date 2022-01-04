/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.intellij.openapi.application.ApplicationManager
 */
package com.github.copilot.openai;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.application.ApplicationManager;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public interface OpenAI {
	public static final Gson GSON = new GsonBuilder().create();
	public static final String DEFAULT_ENGINE_PATH = "/v1/engines/copilot-codex";
	public static final String GITHUB_PROXY_HOST = "https://copilot-proxy.githubusercontent.com";
	public static final String GITHUB_API_COPILOT_TOKEN_URL = "https://api.github.com/copilot_internal/token";
	public static final ZonedDateTime LAST_TELEMETRY_TERMS_UPDATE = ZonedDateTime.of(2021, 10, 14, 0, 0, 0, 0,
			ZoneId.of("UTC+8"));
	public static final int MULTILINE_COMPLETIONS_MAX_DOC_LINES = 8000;
	public static final int OPEN_COPILOT_COMPLETIONS = 10;
	public static final int ON_DEMAND_COPILOT_COMPLETIONS = 2;
	public static final int MAX_PROMPT_LENGTH = 1500;
	public static final double AVG_TOKEN_LENGTH = 2.5;
	public static final int COMPLETION_MIN_OFFSET = 10;
	public static final int HTTP_INCOMPATIBLE_CLIENT = 466;
	public static final long CONNECTION_TIMEOUT_MILLIS = 10000L;
	public static final long RESPONSE_TIMEOUT_MILLIS = 20000L;

	public static float getTemperatureForSamples(int numShots) {
		if (ApplicationManager.getApplication().isUnitTestMode()) {
			return 0.0f;
		}
		if (numShots <= 1) {
			return 0.0f;
		}
		if (numShots < 10) {
			return 0.2f;
		}
		if (numShots < 20) {
			return 0.4f;
		}
		return 0.8f;
	}
}
