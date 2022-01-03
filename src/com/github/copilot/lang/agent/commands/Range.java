/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.commands;

import com.github.copilot.lang.agent.commands.Position;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

public class Range {
    @SerializedName(value="start")
        Position start;
    @SerializedName(value="end")
        Position end;

    public Range(Position start, Position end) {
        if (start == null) {
            throw new IllegalStateException("start cannot be null!");
        }
        if (end == null) {
            throw new IllegalStateException("end cannot be null!");
        }
        if (start == null) {
            throw new NullPointerException("start is marked non-null but is null");
        }
        if (end == null) {
            throw new NullPointerException("end is marked non-null but is null");
        }
        this.start = start;
        this.end = end;
    }

        public Position getStart() {
        Position position = this.start;
        if (position == null) {
            throw new IllegalStateException("position cannot be null!");
        }
        return position;
    }

        public Position getEnd() {
        Position position = this.end;
        if (position == null) {
            throw new IllegalStateException("position cannot be null!");
        }
        return position;
    }

    public void setStart(Position start) {
        if (start == null) {
            throw new IllegalStateException("start cannot be null!");
        }
        if (start == null) {
            throw new NullPointerException("start is marked non-null but is null");
        }
        this.start = start;
    }

    public void setEnd(Position end) {
        if (end == null) {
            throw new IllegalStateException("end cannot be null!");
        }
        if (end == null) {
            throw new NullPointerException("end is marked non-null but is null");
        }
        this.end = end;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Range)) {
            return false;
        }
        Range other = (Range)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Position this$start = this.getStart();
        Position other$start = other.getStart();
        if (this$start == null ? other$start != null : !((Object)this$start).equals(other$start)) {
            return false;
        }
        Position this$end = this.getEnd();
        Position other$end = other.getEnd();
        return !(this$end == null ? other$end != null : !((Object)this$end).equals(other$end));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Range;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Position $start = this.getStart();
        result = result * 59 + ($start == null ? 43 : ((Object)$start).hashCode());
        Position $end = this.getEnd();
        result = result * 59 + ($end == null ? 43 : ((Object)$end).hashCode());
        return result;
    }

    public String toString() {
        return "Range(start=" + this.getStart() + ", end=" + this.getEnd() + ")";
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
            case 2: 
            case 3: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 2: 
            case 3: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "start";
                break;
            }
            case 1: 
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "end";
                break;
            }
            case 2: 
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/lang/agent/commands/Range";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/lang/agent/commands/Range";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "getStart";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[1] = "getEnd";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "<init>";
                break;
            }
            case 2: 
            case 3: {
                break;
            }
            case 4: {
                objectArray = objectArray;
                objectArray[2] = "setStart";
                break;
            }
            case 5: {
                objectArray = objectArray;
                objectArray[2] = "setEnd";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 2: 
            case 3: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

