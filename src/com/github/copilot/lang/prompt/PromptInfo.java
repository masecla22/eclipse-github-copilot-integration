/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.concurrent.Immutable
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang.prompt;

import com.github.copilot.request.BlockMode;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class PromptInfo {
	private final String languageId;
	private final String prompt;
	private final String trailingWhitespace;
	private final int promptLength;
	private final BlockMode blockMode;

	public PromptInfo(String languageId, String prompt, String trailingWhitespace, int promptLength,
			BlockMode blockMode) {
		if (prompt == null) {
			throw new IllegalStateException("prompt cannot be null!");
		}
		if (trailingWhitespace == null) {
			throw new IllegalStateException("trailingWhitespace cannot be null!");
		}
		if (prompt == null) {
			throw new NullPointerException("prompt is marked non-null but is null");
		}
		if (trailingWhitespace == null) {
			throw new NullPointerException("trailingWhitespace is marked non-null but is null");
		}
		this.languageId = languageId;
		this.prompt = prompt;
		this.trailingWhitespace = trailingWhitespace;
		this.promptLength = promptLength;
		this.blockMode = blockMode;
	}

	public String getLanguageId() {
		return this.languageId;
	}

	public String getPrompt() {
		String string = this.prompt;
		if (string == null) {
			throw new IllegalStateException("string cannot be null!");
		}
		return string;
	}

	public String getTrailingWhitespace() {
		String string = this.trailingWhitespace;
		if (string == null) {
			throw new IllegalStateException("string cannot be null!");
		}
		return string;
	}

	public int getPromptLength() {
		return this.promptLength;
	}

	public BlockMode getBlockMode() {
		return this.blockMode;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PromptInfo)) {
			return false;
		}
		PromptInfo other = (PromptInfo) o;
		if (this.getPromptLength() != other.getPromptLength()) {
			return false;
		}
		String this$languageId = this.getLanguageId();
		String other$languageId = other.getLanguageId();
		if (this$languageId == null ? other$languageId != null : !this$languageId.equals(other$languageId)) {
			return false;
		}
		String this$prompt = this.getPrompt();
		String other$prompt = other.getPrompt();
		if (this$prompt == null ? other$prompt != null : !this$prompt.equals(other$prompt)) {
			return false;
		}
		String this$trailingWhitespace = this.getTrailingWhitespace();
		String other$trailingWhitespace = other.getTrailingWhitespace();
		if (this$trailingWhitespace == null ? other$trailingWhitespace != null
				: !this$trailingWhitespace.equals(other$trailingWhitespace)) {
			return false;
		}
		BlockMode this$blockMode = this.getBlockMode();
		BlockMode other$blockMode = other.getBlockMode();
		return !(this$blockMode == null ? other$blockMode != null
				: !(this$blockMode).equals(other$blockMode));
	}

	public int hashCode() {
		int PRIME = 59;
		int result = 1;
		result = result * 59 + this.getPromptLength();
		String $languageId = this.getLanguageId();
		result = result * 59 + ($languageId == null ? 43 : $languageId.hashCode());
		String $prompt = this.getPrompt();
		result = result * 59 + ($prompt == null ? 43 : $prompt.hashCode());
		String $trailingWhitespace = this.getTrailingWhitespace();
		result = result * 59 + ($trailingWhitespace == null ? 43 : $trailingWhitespace.hashCode());
		BlockMode $blockMode = this.getBlockMode();
		result = result * 59 + ($blockMode == null ? 43 : ($blockMode).hashCode());
		return result;
	}

	public String toString() {
		return "PromptInfo(languageId=" + this.getLanguageId() + ", prompt=" + this.getPrompt()
				+ ", trailingWhitespace=" + this.getTrailingWhitespace() + ", promptLength=" + this.getPromptLength()
				+ ", blockMode=" + this.getBlockMode() + ")";
	}

}
