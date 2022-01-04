/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.request;

public enum CompletionType {
	GhostText, OpenCopilot;

	public String getTelemetryPrefix() {
		switch (this) {
		case GhostText: {
			return "ghostText";
		}
		case OpenCopilot: {
			return "solution";
		}
		}
		throw new IllegalStateException("Unknown completion type: " + this);
	}

	public String getTelemetryPropertyValue() {
		switch (this) {
		case GhostText: {
			return "ghostText";
		}
		case OpenCopilot: {
			return "synthesize";
		}
		}
		throw new IllegalStateException("Unknown completion type: " + this);
	}

	public String getOpenAiIntent() {
		switch (this) {
		case GhostText: {
			return "copilot-ghost";
		}
		case OpenCopilot: {
			return "copilot-panel";
		}
		}
		return null;
	}
}
