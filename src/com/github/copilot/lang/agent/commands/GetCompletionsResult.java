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
import org.jetbrains.annotations.NotNull;

public class GetCompletionsResult {
    @SerializedName(value="completions")
        List<Completion> completions;

    public GetCompletionsResult(List<Completion> completions) {
        if (completions == null) {
            GetCompletionsResult.$$$reportNull$$$0(0);
        }
        if (completions == null) {
            throw new NullPointerException("completions is marked non-null but is null");
        }
        this.completions = completions;
    }

        public List<Completion> getCompletions() {
        List<Completion> list = this.completions;
        if (list == null) {
            GetCompletionsResult.$$$reportNull$$$0(1);
        }
        return list;
    }

    public void setCompletions(List<Completion> completions) {
        if (completions == null) {
            GetCompletionsResult.$$$reportNull$$$0(2);
        }
        if (completions == null) {
            throw new NullPointerException("completions is marked non-null but is null");
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
        int PRIME = 59;
        int result = 1;
        List<Completion> $completions = this.getCompletions();
        result = result * 59 + ($completions == null ? 43 : ((Object)$completions).hashCode());
        return result;
    }

    public String toString() {
        return "GetCompletionsResult(completions=" + this.getCompletions() + ")";
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
            case 1: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 1: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "completions";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/lang/agent/commands/GetCompletionsResult";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/lang/agent/commands/GetCompletionsResult";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "getCompletions";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "<init>";
                break;
            }
            case 1: {
                break;
            }
            case 2: {
                objectArray = objectArray;
                objectArray[2] = "setCompletions";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 1: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
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
                Completion.$$$reportNull$$$0(0);
            }
            if (text == null) {
                Completion.$$$reportNull$$$0(1);
            }
            if (range == null) {
                Completion.$$$reportNull$$$0(2);
            }
            if (displayText == null) {
                Completion.$$$reportNull$$$0(3);
            }
            if (position == null) {
                Completion.$$$reportNull$$$0(4);
            }
            if (uuid == null) {
                throw new NullPointerException("uuid is marked non-null but is null");
            }
            if (text == null) {
                throw new NullPointerException("text is marked non-null but is null");
            }
            if (range == null) {
                throw new NullPointerException("range is marked non-null but is null");
            }
            if (displayText == null) {
                throw new NullPointerException("displayText is marked non-null but is null");
            }
            if (position == null) {
                throw new NullPointerException("position is marked non-null but is null");
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
                Completion.$$$reportNull$$$0(5);
            }
            return string;
        }

                public String getText() {
            String string = this.text;
            if (string == null) {
                Completion.$$$reportNull$$$0(6);
            }
            return string;
        }

                public Range getRange() {
            Range range = this.range;
            if (range == null) {
                Completion.$$$reportNull$$$0(7);
            }
            return range;
        }

                public String getDisplayText() {
            String string = this.displayText;
            if (string == null) {
                Completion.$$$reportNull$$$0(8);
            }
            return string;
        }

                public Position getPosition() {
            Position position = this.position;
            if (position == null) {
                Completion.$$$reportNull$$$0(9);
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
            int PRIME = 59;
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

        private static /* synthetic */ void $$$reportNull$$$0(int n) {
            RuntimeException runtimeException;
            Object[] objectArray;
            Object[] objectArray2;
            int n2;
            String string;
            switch (n) {
                default: {
                    string = "Argument for parameter '%s' of %s.%s must not be null";
                    break;
                }
                case 5: 
                case 6: 
                case 7: 
                case 8: 
                case 9: {
                    string = "method %s.%s must not return null";
                    break;
                }
            }
            switch (n) {
                default: {
                    n2 = 3;
                    break;
                }
                case 5: 
                case 6: 
                case 7: 
                case 8: 
                case 9: {
                    n2 = 2;
                    break;
                }
            }
            Object[] objectArray3 = new Object[n2];
            switch (n) {
                default: {
                    objectArray2 = objectArray3;
                    objectArray3[0] = "uuid";
                    break;
                }
                case 1: {
                    objectArray2 = objectArray3;
                    objectArray3[0] = "text";
                    break;
                }
                case 2: {
                    objectArray2 = objectArray3;
                    objectArray3[0] = "range";
                    break;
                }
                case 3: {
                    objectArray2 = objectArray3;
                    objectArray3[0] = "displayText";
                    break;
                }
                case 4: {
                    objectArray2 = objectArray3;
                    objectArray3[0] = "position";
                    break;
                }
                case 5: 
                case 6: 
                case 7: 
                case 8: 
                case 9: {
                    objectArray2 = objectArray3;
                    objectArray3[0] = "com/github/copilot/lang/agent/commands/GetCompletionsResult$Completion";
                    break;
                }
            }
            switch (n) {
                default: {
                    objectArray = objectArray2;
                    objectArray2[1] = "com/github/copilot/lang/agent/commands/GetCompletionsResult$Completion";
                    break;
                }
                case 5: {
                    objectArray = objectArray2;
                    objectArray2[1] = "getUuid";
                    break;
                }
                case 6: {
                    objectArray = objectArray2;
                    objectArray2[1] = "getText";
                    break;
                }
                case 7: {
                    objectArray = objectArray2;
                    objectArray2[1] = "getRange";
                    break;
                }
                case 8: {
                    objectArray = objectArray2;
                    objectArray2[1] = "getDisplayText";
                    break;
                }
                case 9: {
                    objectArray = objectArray2;
                    objectArray2[1] = "getPosition";
                    break;
                }
            }
            switch (n) {
                default: {
                    objectArray = objectArray;
                    objectArray[2] = "<init>";
                    break;
                }
                case 5: 
                case 6: 
                case 7: 
                case 8: 
                case 9: {
                    break;
                }
            }
            String string2 = String.format(string, objectArray);
            switch (n) {
                default: {
                    runtimeException = new IllegalArgumentException(string2);
                    break;
                }
                case 5: 
                case 6: 
                case 7: 
                case 8: 
                case 9: {
                    runtimeException = new IllegalStateException(string2);
                    break;
                }
            }
            throw runtimeException;
        }
    }
}

