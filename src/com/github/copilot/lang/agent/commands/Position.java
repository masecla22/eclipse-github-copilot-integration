/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  com.intellij.openapi.util.text.StringUtil
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.commands;

import com.github.copilot.request.LineInfo;
import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

public class Position {
    @SerializedName(value="line")
    int line;
    @SerializedName(value="character")
    int character;

    public Position(LineInfo lineInfo) {
        if (lineInfo == null) {
            Position.$$$reportNull$$$0(0);
        }
        this(lineInfo.getLineNumber(), lineInfo.getColumnOffset());
    }

    public int toOffset(String text) {
        if (text == null) {
            Position.$$$reportNull$$$0(1);
        }
        return StringUtil.lineColToOffset((CharSequence)text, (int)this.line, (int)this.character);
    }

    public int getLine() {
        return this.line;
    }

    public int getCharacter() {
        return this.character;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setCharacter(int character) {
        this.character = character;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        Position other = (Position)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getLine() != other.getLine()) {
            return false;
        }
        return this.getCharacter() == other.getCharacter();
    }

    protected boolean canEqual(Object other) {
        return other instanceof Position;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getLine();
        result = result * 59 + this.getCharacter();
        return result;
    }

    public String toString() {
        return "Position(line=" + this.getLine() + ", character=" + this.getCharacter() + ")";
    }

    public Position(int line, int character) {
        this.line = line;
        this.character = character;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2;
        Object[] objectArray3 = new Object[3];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "lineInfo";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "text";
                break;
            }
        }
        objectArray2[1] = "com/github/copilot/lang/agent/commands/Position";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "<init>";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[2] = "toOffset";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

