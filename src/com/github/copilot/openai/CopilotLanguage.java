/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.openai;

public enum CopilotLanguage {
	Python, Java, Unknown;

	private final String enginePath;

	private CopilotLanguage() {
		this("/v1/engines/copilot-codex");
	}

	private CopilotLanguage(String enginePath) {
		if (enginePath == null) {
			throw new IllegalStateException("enginePath cannot be null!");
		}
		this.enginePath = enginePath;
	}

	public String getEngineName() {
		String path = this.getEnginePath();
		int index = path.lastIndexOf(47);
		return index == -1 ? path : path.substring(index + 1);
	}

	public String getEnginePath() {
		String string = this.enginePath;
		if (string == null) {
			throw new IllegalStateException("string cannot be null!");
		}
		return string;
	}

}
