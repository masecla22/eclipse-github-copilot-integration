/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.commands;

import com.github.copilot.lang.agent.commands.Position;
import com.github.copilot.lang.agent.commands.Range;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetCompletionsResult {
    @SerializedName(value="completions")
        List<Completion> completions;

    public GetCompletionsResult(List<Completion> completions) {
        if (completions == null) {
            throw new IllegalStateException("completions cannot be null!");
        }
        this.completions = completions;
    }

        public List<Completion> getCompletions() {
        List<Completion> list = this.completions;
        if (list == null) {
            throw new IllegalStateException("list cannot be null!");
        }
        return list;
    }

    public void setCompletions(List<Completion> completions) {
        if (completions == null) {
            throw new IllegalStateException("completions cannot be null!");
        }
        this.completions = completions;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GetCompletionsResult)) {
            return false;
        }
        GetCompletionsResult other = (GetCompletionsResult)o;
        if (!other.canEqual(this)) {
            return false;
        }
        List<Completion> this$completions = this.getCompletions();
        List<Completion> other$completions = other.getCompletions();
        return !(this$completions == null ? other$completions != null : !((Object)this$completions).equals(other$completions));
    }

    protected boolean canEqual(Object other) {
        return other instanceof GetCompletionsResult;
    }

    public int hashCode() {
        int result = 1;
        List<Completion> $completions = this.getCompletions();
        result = result * 59 + ($completions == null ? 43 : ((Object)$completions).hashCode());
        return result;
    }

    public String toString() {
        return "GetCompletionsResult(completions=" + this.getCompletions() + ")";
    }

    

    public static final class Completion {
        @SerializedName(value="uuid")
                private final String uuid;
        @SerializedName(value="text")
                private final String text;
        @SerializedName(value="range")
                private final Range range;
        @SerializedName(value="displayText")
                private final String displayText;
        @SerializedName(value="position")
                private final Position position;

        public Completion(String uuid, String text, Range range, String displayText, Position position) {
            if (uuid == null) {
                throw new IllegalStateException("uuid cannot be null!");
            }
            if (text == null) {
                throw new IllegalStateException("text cannot be null!");
            }
            if (range == null) {
                throw new IllegalStateException("range cannot be null!");
            }
            if (displayText == null) {
                throw new IllegalStateException("displayText cannot be null!");
            }
            if (position == null) {
                throw new IllegalStateException("position cannot be null!");
            }
            this.uuid = uuid;
            this.text = text;
            this.range = range;
            this.displayText = displayText;
            this.position = position;
        }

                public String getUuid() {
            String string = this.uuid;
            if (string == null) {
                throw new IllegalStateException("string cannot be null!");
            }
            return string;
        }

                public String getText() {
            String string = this.text;
            if (string == null) {
                throw new IllegalStateException("string cannot be null!");
            }
            return string;
        }

                public Range getRange() {
            Range range = this.range;
            if (range == null) {
                throw new IllegalStateException("range cannot be null!");
            }
            return range;
        }

                public String getDisplayText() {
            String string = this.displayText;
            if (string == null) {
                throw new IllegalStateException("string cannot be null!");
            }
            return string;
        }

                public Position getPosition() {
            Position position = this.position;
            if (position == null) {
                throw new IllegalStateException("position cannot be null!");
            }
            return position;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Completion)) {
                return false;
            }
            Completion other = (Completion)o;
            String this$uuid = this.getUuid();
            String other$uuid = other.getUuid();
            if (this$uuid == null ? other$uuid != null : !this$uuid.equals(other$uuid)) {
                return false;
            }
            String this$text = this.getText();
            String other$text = other.getText();
            if (this$text == null ? other$text != null : !this$text.equals(other$text)) {
                return false;
            }
            Range this$range = this.getRange();
            Range other$range = other.getRange();
            if (this$range == null ? other$range != null : !((Object)this$range).equals(other$range)) {
                return false;
            }
            String this$displayText = this.getDisplayText();
            String other$displayText = other.getDisplayText();
            if (this$displayText == null ? other$displayText != null : !this$displayText.equals(other$displayText)) {
                return false;
            }
            Position this$position = this.getPosition();
            Position other$position = other.getPosition();
            return !(this$position == null ? other$position != null : !((Object)this$position).equals(other$position));
        }

        public int hashCode() {
            int result = 1;
            String $uuid = this.getUuid();
            result = result * 59 + ($uuid == null ? 43 : $uuid.hashCode());
            String $text = this.getText();
            result = result * 59 + ($text == null ? 43 : $text.hashCode());
            Range $range = this.getRange();
            result = result * 59 + ($range == null ? 43 : ((Object)$range).hashCode());
            String $displayText = this.getDisplayText();
            result = result * 59 + ($displayText == null ? 43 : $displayText.hashCode());
            Position $position = this.getPosition();
            result = result * 59 + ($position == null ? 43 : ((Object)$position).hashCode());
            return result;
        }

        public String toString() {
            return "GetCompletionsResult.Completion(uuid=" + this.getUuid() + ", text=" + this.getText() + ", range=" + this.getRange() + ", displayText=" + this.getDisplayText() + ", position=" + this.getPosition() + ")";
        }
    }
}

