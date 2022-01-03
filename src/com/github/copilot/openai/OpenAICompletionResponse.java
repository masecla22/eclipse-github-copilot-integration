/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.openai;

import com.github.copilot.util.String2DoubleMap;
import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public final class OpenAICompletionResponse {
    @SerializedName(value="id")
    public String id;
    @SerializedName(value="created")
    public int createdTimestamp;
    @SerializedName(value="model")
    public String model;
    @SerializedName(value="choices")
    public Choice[] choices;

    public String getId() {
        return this.id;
    }

    public int getCreatedTimestamp() {
        return this.createdTimestamp;
    }

    public String getModel() {
        return this.model;
    }

    public Choice[] getChoices() {
        return this.choices;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreatedTimestamp(int createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setChoices(Choice[] choices) {
        this.choices = choices;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OpenAICompletionResponse)) {
            return false;
        }
        OpenAICompletionResponse other = (OpenAICompletionResponse)o;
        if (this.getCreatedTimestamp() != other.getCreatedTimestamp()) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$model = this.getModel();
        String other$model = other.getModel();
        if (this$model == null ? other$model != null : !this$model.equals(other$model)) {
            return false;
        }
        return Arrays.deepEquals(this.getChoices(), other.getChoices());
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getCreatedTimestamp();
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $model = this.getModel();
        result = result * 59 + ($model == null ? 43 : $model.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getChoices());
        return result;
    }

    public String toString() {
        return "OpenAICompletionResponse(id=" + this.getId() + ", createdTimestamp=" + this.getCreatedTimestamp() + ", model=" + this.getModel() + ", choices=" + Arrays.deepToString(this.getChoices()) + ")";
    }

    public static final class Logprobs {
        @SerializedName(value="tokens")
        public String[] tokens;
        @SerializedName(value="text_offset")
        public int[] textOffset;
        @SerializedName(value="token_logprobs")
        public double[] tokenLogprobs;
        @SerializedName(value="top_logprobs")
        public List<String2DoubleMap> topLogprobs;

        public String[] getTokens() {
            return this.tokens;
        }

        public int[] getTextOffset() {
            return this.textOffset;
        }

        public double[] getTokenLogprobs() {
            return this.tokenLogprobs;
        }

        public List<String2DoubleMap> getTopLogprobs() {
            return this.topLogprobs;
        }

        public void setTokens(String[] tokens) {
            this.tokens = tokens;
        }

        public void setTextOffset(int[] textOffset) {
            this.textOffset = textOffset;
        }

        public void setTokenLogprobs(double[] tokenLogprobs) {
            this.tokenLogprobs = tokenLogprobs;
        }

        public void setTopLogprobs(List<String2DoubleMap> topLogprobs) {
            this.topLogprobs = topLogprobs;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Logprobs)) {
                return false;
            }
            Logprobs other = (Logprobs)o;
            if (!Arrays.deepEquals(this.getTokens(), other.getTokens())) {
                return false;
            }
            if (!Arrays.equals(this.getTextOffset(), other.getTextOffset())) {
                return false;
            }
            if (!Arrays.equals(this.getTokenLogprobs(), other.getTokenLogprobs())) {
                return false;
            }
            List<String2DoubleMap> this$topLogprobs = this.getTopLogprobs();
            List<String2DoubleMap> other$topLogprobs = other.getTopLogprobs();
            return !(this$topLogprobs == null ? other$topLogprobs != null : !((Object)this$topLogprobs).equals(other$topLogprobs));
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            result = result * 59 + Arrays.deepHashCode(this.getTokens());
            result = result * 59 + Arrays.hashCode(this.getTextOffset());
            result = result * 59 + Arrays.hashCode(this.getTokenLogprobs());
            List<String2DoubleMap> $topLogprobs = this.getTopLogprobs();
            result = result * 59 + ($topLogprobs == null ? 43 : ((Object)$topLogprobs).hashCode());
            return result;
        }

        public String toString() {
            return "OpenAICompletionResponse.Logprobs(tokens=" + Arrays.deepToString(this.getTokens()) + ", textOffset=" + Arrays.toString(this.getTextOffset()) + ", tokenLogprobs=" + Arrays.toString(this.getTokenLogprobs()) + ", topLogprobs=" + this.getTopLogprobs() + ")";
        }
    }

    public static final class Choice {
        @SerializedName(value="text")
        public String text;
        @SerializedName(value="index")
        public int index;
        @SerializedName(value="logprobs")
                public Logprobs logprobs;
        @SerializedName(value="finish_reason")
                public String finishReason;

        boolean isFinished() {
            return this.finishReason != null && !this.finishReason.isEmpty();
        }

        public String getText() {
            return this.text;
        }

        public int getIndex() {
            return this.index;
        }

                public Logprobs getLogprobs() {
            return this.logprobs;
        }

                public String getFinishReason() {
            return this.finishReason;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void setLogprobs(Logprobs logprobs) {
            this.logprobs = logprobs;
        }

        public void setFinishReason(String finishReason) {
            this.finishReason = finishReason;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Choice)) {
                return false;
            }
            Choice other = (Choice)o;
            if (this.getIndex() != other.getIndex()) {
                return false;
            }
            String this$text = this.getText();
            String other$text = other.getText();
            if (this$text == null ? other$text != null : !this$text.equals(other$text)) {
                return false;
            }
            Logprobs this$logprobs = this.getLogprobs();
            Logprobs other$logprobs = other.getLogprobs();
            if (this$logprobs == null ? other$logprobs != null : !((Object)this$logprobs).equals(other$logprobs)) {
                return false;
            }
            String this$finishReason = this.getFinishReason();
            String other$finishReason = other.getFinishReason();
            return !(this$finishReason == null ? other$finishReason != null : !this$finishReason.equals(other$finishReason));
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            result = result * 59 + this.getIndex();
            String $text = this.getText();
            result = result * 59 + ($text == null ? 43 : $text.hashCode());
            Logprobs $logprobs = this.getLogprobs();
            result = result * 59 + ($logprobs == null ? 43 : ((Object)$logprobs).hashCode());
            String $finishReason = this.getFinishReason();
            result = result * 59 + ($finishReason == null ? 43 : $finishReason.hashCode());
            return result;
        }

        public String toString() {
            return "OpenAICompletionResponse.Choice(text=" + this.getText() + ", index=" + this.getIndex() + ", logprobs=" + this.getLogprobs() + ", finishReason=" + this.getFinishReason() + ")";
        }
    }
}

