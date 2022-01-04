/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.compress.utils.Lists
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.rpc;

import java.util.ArrayList;
import java.util.List;

public class StoringJsonRpcMessageHandler implements JsonRpcMessageHandler {
	private final List<String> messages = new ArrayList<>();

	@Override
	public void handleJsonMessage(String message) {
		if (message == null) {
			throw new IllegalStateException("message cannot be null!");
		}
		this.messages.add(message);
	}

	public List<String> getMessages() {
		return this.messages;
	}

}
