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
            throw new IllegalStateException("lineInfo cannot be null!");
        }
        this(lineInfo.getLineNumber(), lineInfo.getColumnOffset());
    }

    public int toOffset(String text) {
        if (text == null) {
            throw new IllegalStateException("text cannot be null!");
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

    
}

