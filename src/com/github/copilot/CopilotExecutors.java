package com.github.copilot;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class CopilotExecutors {
	private static final Executor EXECUTOR = Executors.newFixedThreadPool(5);

	private CopilotExecutors() {
	}

	public static Executor getExecutor() {
		return EXECUTOR;
	}
}
