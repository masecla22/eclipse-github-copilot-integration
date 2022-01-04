/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.DynamicBundle
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.PropertyKey
 */
package com.github.copilot;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.PropertyKey;

public final class CopilotBundle extends DynamicBundle {
	public static final CopilotBundle INSTANCE = new CopilotBundle();

	private CopilotBundle() {
		super("copilot.copilot");
	}

	public static String get(
			@PropertyKey(resourceBundle = "copilot.copilot") @PropertyKey(resourceBundle = "copilot.copilot") String key) {
		if (key == null) {
			throw new IllegalStateException("key cannot be null!");
		}
		return INSTANCE.getMessage(key, new Object[0]);
	}

	public static String get(
			@PropertyKey(resourceBundle = "copilot.copilot") @PropertyKey(resourceBundle = "copilot.copilot") String key,
			Object... params) {
		if (key == null) {
			throw new IllegalStateException("key cannot be null!");
		}
		return INSTANCE.getMessage(key, params);
	}

}
