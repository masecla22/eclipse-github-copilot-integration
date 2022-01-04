/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.openai;

import com.google.gson.annotations.SerializedName;
import java.util.Arrays;

public final class OpenAiRequestBody {
	@SerializedName(value = "prompt")
	public String prompt;
	@SerializedName(value = "max_tokens")
	public int maxTokens;
	@SerializedName(value = "temperature")
	public double temperature;
	@SerializedName(value = "top_p")
	public int topP;
	@SerializedName(value = "n")
	public int completionCount;
	@SerializedName(value = "logprobs")
	public int logprobs;
	@SerializedName(value = "stream")
	public boolean stream;
	@SerializedName(value = "stop")
	public String[] stops;
	@SerializedName(value = "feature_flags")
	public String[] experimentalFeatures;
	@SerializedName(value = "extra")
	public Extra extra;

	/*
	 * WARNING - void declaration
	 */
	public OpenAiRequestBody(String prompt, int maxTokens, double temperature, int topP, int completionCount,
			int logprobs, boolean stream, String[] stops, boolean useServerSideIndentation, int n) {
		void nextLineIndent;
		if (prompt == null) {
			throw new IllegalStateException("prompt cannot be null!");
		}
		this.prompt = prompt;
		this.maxTokens = maxTokens;
		this.temperature = temperature;
		this.topP = topP;
		this.completionCount = completionCount;
		this.logprobs = logprobs;
		this.stream = stream;
		this.stops = stops;
		if (useServerSideIndentation && nextLineIndent >= 0) {
			this.experimentalFeatures = new String[] { "trim_to_block" };
			this.extra = new Extra((int) nextLineIndent);
		}
	}

	public String getPrompt() {
		return this.prompt;
	}

	public int getMaxTokens() {
		return this.maxTokens;
	}

	public double getTemperature() {
		return this.temperature;
	}

	public int getTopP() {
		return this.topP;
	}

	public int getCompletionCount() {
		return this.completionCount;
	}

	public int getLogprobs() {
		return this.logprobs;
	}

	public boolean isStream() {
		return this.stream;
	}

	public String[] getStops() {
		return this.stops;
	}

	public String[] getExperimentalFeatures() {
		return this.experimentalFeatures;
	}

	public Extra getExtra() {
		return this.extra;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public void setMaxTokens(int maxTokens) {
		this.maxTokens = maxTokens;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public void setTopP(int topP) {
		this.topP = topP;
	}

	public void setCompletionCount(int completionCount) {
		this.completionCount = completionCount;
	}

	public void setLogprobs(int logprobs) {
		this.logprobs = logprobs;
	}

	public void setStream(boolean stream) {
		this.stream = stream;
	}

	public void setStops(String[] stops) {
		this.stops = stops;
	}

	public void setExperimentalFeatures(String[] experimentalFeatures) {
		this.experimentalFeatures = experimentalFeatures;
	}

	public void setExtra(Extra extra) {
		this.extra = extra;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof OpenAiRequestBody)) {
			return false;
		}
		OpenAiRequestBody other = (OpenAiRequestBody) o;
		if (this.getMaxTokens() != other.getMaxTokens()) {
			return false;
		}
		if (Double.compare(this.getTemperature(), other.getTemperature()) != 0) {
			return false;
		}
		if (this.getTopP() != other.getTopP()) {
			return false;
		}
		if (this.getCompletionCount() != other.getCompletionCount()) {
			return false;
		}
		if (this.getLogprobs() != other.getLogprobs()) {
			return false;
		}
		if (this.isStream() != other.isStream()) {
			return false;
		}
		String this$prompt = this.getPrompt();
		String other$prompt = other.getPrompt();
		if (this$prompt == null ? other$prompt != null : !this$prompt.equals(other$prompt)) {
			return false;
		}
		if (!Arrays.deepEquals(this.getStops(), other.getStops())) {
			return false;
		}
		if (!Arrays.deepEquals(this.getExperimentalFeatures(), other.getExperimentalFeatures())) {
			return false;
		}
		Extra this$extra = this.getExtra();
		Extra other$extra = other.getExtra();
		return !(this$extra == null ? other$extra != null : !this$extra.equals(other$extra));
	}

	public int hashCode() {
		int result = 1;
		result = result * 59 + this.getMaxTokens();
		long $temperature = Double.doubleToLongBits(this.getTemperature());
		result = result * 59 + (int) ($temperature >>> 32 ^ $temperature);
		result = result * 59 + this.getTopP();
		result = result * 59 + this.getCompletionCount();
		result = result * 59 + this.getLogprobs();
		result = result * 59 + (this.isStream() ? 79 : 97);
		String $prompt = this.getPrompt();
		result = result * 59 + ($prompt == null ? 43 : $prompt.hashCode());
		result = result * 59 + Arrays.deepHashCode(this.getStops());
		result = result * 59 + Arrays.deepHashCode(this.getExperimentalFeatures());
		Extra $extra = this.getExtra();
		result = result * 59 + ($extra == null ? 43 : $extra.hashCode());
		return result;
	}

	public String toString() {
		return "OpenAiRequestBody(prompt=" + this.getPrompt() + ", maxTokens=" + this.getMaxTokens() + ", temperature="
				+ this.getTemperature() + ", topP=" + this.getTopP() + ", completionCount=" + this.getCompletionCount()
				+ ", logprobs=" + this.getLogprobs() + ", stream=" + this.isStream() + ", stops="
				+ Arrays.deepToString(this.getStops()) + ", experimentalFeatures="
				+ Arrays.deepToString(this.getExperimentalFeatures()) + ", extra=" + this.getExtra() + ")";
	}

	public static final class Extra {
		@SerializedName(value = "next_indent")
		public int nextLineIndent;

		public Extra() {
		}

		public Extra(int nextLineIndent) {
			this.nextLineIndent = nextLineIndent;
		}

		public String toString() {
			return "OpenAiRequestBody.Extra(nextLineIndent=" + this.nextLineIndent + ")";
		}
	}
}
