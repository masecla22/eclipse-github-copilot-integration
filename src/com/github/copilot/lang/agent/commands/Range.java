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
        this.start = start;
    }

    public void setEnd(Position end) {
        if (end == null) {
            throw new IllegalStateException("end cannot be null!");
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

    
}

