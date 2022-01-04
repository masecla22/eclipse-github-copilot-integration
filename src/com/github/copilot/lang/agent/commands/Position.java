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

public class Position {
	@SerializedName(value = "line")
	int line;
	@SerializedName(value = "character")
	int character;

	public Position(LineInfo lineInfo) {
		this(lineInfo.getLineNumber(), lineInfo.getColumnOffset());
	}

	public int toOffset(String text) {
		if (text == null) {
			throw new IllegalStateException("text cannot be null!");
		}
		return lineColToOffset(text, this.line, this.character);
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
		Position other = (Position) o;
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

	private int lineColToOffset(CharSequence text, int line, int col) {
		int curLine = 0;
		int offset = 0;
		while (line != curLine) {
			if (offset == text.length())
				return -1;
			char c = text.charAt(offset);
			if (c == '\n') {
				curLine++;
			} else if (c == '\r') {
				curLine++;
				if (offset < text.length() - 1 && text.charAt(offset + 1) == '\n') {
					offset++;
				}
			}
			offset++;
		}
		return offset + col;
	}

	public int hashCode() {
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
