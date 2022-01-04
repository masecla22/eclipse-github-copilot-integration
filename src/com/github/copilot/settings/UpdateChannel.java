/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.util.ui.PresentableEnum
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.settings;

import com.github.copilot.CopilotBundle;
import com.intellij.util.ui.PresentableEnum;

public enum UpdateChannel implements PresentableEnum {
	Stable(null), Nightly("https://plugins.jetbrains.com/plugins/nightly/17718");

	private final String channelUrl;

	public String getPresentableText() {
		switch (this) {
		case Stable: {
			return CopilotBundle.get("applicationConfigurable.channel.stableChannel");
		}
		case Nightly: {
			return CopilotBundle.get("applicationConfigurable.channel.nightlyChannel");
		}
		}
		throw new IllegalStateException("Unexpected channel: " + this);
	}

	private UpdateChannel(String channelUrl) {
		this.channelUrl = channelUrl;
	}

	public String getChannelUrl() {
		return this.channelUrl;
	}
}
