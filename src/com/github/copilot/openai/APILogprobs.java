/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.openai;

import com.github.copilot.openai.OpenAIServiceImpl;
import com.github.copilot.openai.StringDoublePair;
import com.google.gson.Gson;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class APILogprobs {
    @Nullable
    private final List<Integer> textOffset;
    @Nullable
    private final List<Double> tokenLogprobs;
    @Nullable
    private final List<StringDoublePair> topLogprobs;
    @Nullable
    private final List<String> tokens;

    @NotNull
    Map<String, String> createTelemetryJson() {
        Gson gson = OpenAIServiceImpl.GSON;
        String fallback = "unset";
        Map<String, String> map = Map.of("logprobs_text_offset", this.textOffset == null ? fallback : gson.toJson(this.textOffset), "logprobs_token_logprobs", this.tokenLogprobs == null ? fallback : gson.toJson(this.tokenLogprobs), "logprobs_top_logprobs", this.topLogprobs == null ? fallback : gson.toJson(this.topLogprobs), "logprobs_tokens", this.tokens == null ? fallback : gson.toJson(this.tokens));
        if (map == null) {
            APILogprobs.$$$reportNull$$$0(0);
        }
        return map;
    }

    @Nullable
    Double calculateMeanLogprob() {
        List<Double> values = this.tokenLogprobs;
        if (values == null || values.isEmpty()) {
            return null;
        }
        double logProbSum = 0.0;
        double numTokens = 0.0;
        int iterLimit = 50;
        for (int i = 0; i < values.size() - 1 && i < iterLimit; ++i) {
            Double value = values.get(i);
            logProbSum += value.doubleValue();
            numTokens += 1.0;
        }
        return numTokens > 0.0 ? Double.valueOf(logProbSum / numTokens) : null;
    }

    public APILogprobs(@Nullable List<Integer> textOffset, @Nullable List<Double> tokenLogprobs, @Nullable List<StringDoublePair> topLogprobs, @Nullable List<String> tokens) {
        this.textOffset = textOffset;
        this.tokenLogprobs = tokenLogprobs;
        this.topLogprobs = topLogprobs;
        this.tokens = tokens;
    }

    @Nullable
    public List<Integer> getTextOffset() {
        return this.textOffset;
    }

    @Nullable
    public List<Double> getTokenLogprobs() {
        return this.tokenLogprobs;
    }

    @Nullable
    public List<StringDoublePair> getTopLogprobs() {
        return this.topLogprobs;
    }

    @Nullable
    public List<String> getTokens() {
        return this.tokens;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof APILogprobs)) {
            return false;
        }
        APILogprobs other = (APILogprobs)o;
        List<Integer> this$textOffset = this.getTextOffset();
        List<Integer> other$textOffset = other.getTextOffset();
        if (this$textOffset == null ? other$textOffset != null : !((Object)this$textOffset).equals(other$textOffset)) {
            return false;
        }
        List<Double> this$tokenLogprobs = this.getTokenLogprobs();
        List<Double> other$tokenLogprobs = other.getTokenLogprobs();
        if (this$tokenLogprobs == null ? other$tokenLogprobs != null : !((Object)this$tokenLogprobs).equals(other$tokenLogprobs)) {
            return false;
        }
        List<StringDoublePair> this$topLogprobs = this.getTopLogprobs();
        List<StringDoublePair> other$topLogprobs = other.getTopLogprobs();
        if (this$topLogprobs == null ? other$topLogprobs != null : !((Object)this$topLogprobs).equals(other$topLogprobs)) {
            return false;
        }
        List<String> this$tokens = this.getTokens();
        List<String> other$tokens = other.getTokens();
        return !(this$tokens == null ? other$tokens != null : !((Object)this$tokens).equals(other$tokens));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<Integer> $textOffset = this.getTextOffset();
        result = result * 59 + ($textOffset == null ? 43 : ((Object)$textOffset).hashCode());
        List<Double> $tokenLogprobs = this.getTokenLogprobs();
        result = result * 59 + ($tokenLogprobs == null ? 43 : ((Object)$tokenLogprobs).hashCode());
        List<StringDoublePair> $topLogprobs = this.getTopLogprobs();
        result = result * 59 + ($topLogprobs == null ? 43 : ((Object)$topLogprobs).hashCode());
        List<String> $tokens = this.getTokens();
        result = result * 59 + ($tokens == null ? 43 : ((Object)$tokens).hashCode());
        return result;
    }

    public String toString() {
        return "APILogprobs(textOffset=" + this.getTextOffset() + ", tokenLogprobs=" + this.getTokenLogprobs() + ", topLogprobs=" + this.getTopLogprobs() + ", tokens=" + this.getTokens() + ")";
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "com/github/copilot/openai/APILogprobs", "createTelemetryJson"));
    }
}

