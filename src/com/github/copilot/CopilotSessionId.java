package com.github.copilot;

import java.util.UUID;

public final class CopilotSessionId {
	public static final String SESSION_ID = UUID.randomUUID().toString();

	private CopilotSessionId() {
	}
}
