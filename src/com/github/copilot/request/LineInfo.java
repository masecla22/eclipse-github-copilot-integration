/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.editor.Document
 *  com.intellij.openapi.util.TextRange
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.request;

import com.github.copilot.editor.CopilotEditorUtil;
import com.github.copilot.util.CopilotStringUtil;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

public final class LineInfo {
    private final int lineCount;
    private final int lineNumber;
    private final int lineStartOffset;
    private final int columnOffset;
        private final String line;
    private final int nextLineIndent;

        public static LineInfo create(Document document, int offset) {
        if (document == null) {
            LineInfo.$$$reportNull$$$0(0);
        }
        int line = document.getLineNumber(offset);
        TextRange lineRange = TextRange.create((int)document.getLineStartOffset(line), (int)document.getLineEndOffset(line));
        return new LineInfo(document.getLineCount(), line, lineRange.getStartOffset(), offset - lineRange.getStartOffset(), document.getText(lineRange), LineInfo.calculateNextLineIndent(document, offset));
    }

        public String getLinePrefix() {
        String string = this.line.substring(0, this.columnOffset);
        if (string == null) {
            LineInfo.$$$reportNull$$$0(1);
        }
        return string;
    }

        public String getLineSuffix() {
        String string = this.line.substring(this.columnOffset);
        if (string == null) {
            LineInfo.$$$reportNull$$$0(2);
        }
        return string;
    }

    public boolean isBlankLine() {
        return this.line.isBlank();
    }

        public String getWhitespaceBeforeCursor() {
        String string = CopilotStringUtil.trailingWhitespace(this.getLinePrefix());
        if (string == null) {
            LineInfo.$$$reportNull$$$0(3);
        }
        return string;
    }

    public int getLineEndOffset() {
        return this.getLineStartOffset() + this.line.length();
    }

    private static int calculateNextLineIndent(Document document, int offset) {
        if (document == null) {
            LineInfo.$$$reportNull$$$0(4);
        }
        int maxLines = document.getLineCount();
        for (int line = document.getLineNumber(offset) + 1; line < maxLines; ++line) {
            String lineContent;
            int end;
            int start = document.getLineStartOffset(line);
            if (start == (end = document.getLineEndOffset(line)) || (lineContent = document.getText(TextRange.create((int)start, (int)end))).isBlank()) continue;
            return CopilotEditorUtil.whitespacePrefixLength(lineContent);
        }
        return -1;
    }

    public LineInfo(int lineCount, int lineNumber, int lineStartOffset, int columnOffset, String line, int nextLineIndent) {
        if (line == null) {
            LineInfo.$$$reportNull$$$0(5);
        }
        if (line == null) {
            throw new NullPointerException("line is marked non-null but is null");
        }
        this.lineCount = lineCount;
        this.lineNumber = lineNumber;
        this.lineStartOffset = lineStartOffset;
        this.columnOffset = columnOffset;
        this.line = line;
        this.nextLineIndent = nextLineIndent;
    }

    public int getLineCount() {
        return this.lineCount;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public int getLineStartOffset() {
        return this.lineStartOffset;
    }

    public int getColumnOffset() {
        return this.columnOffset;
    }

        public String getLine() {
        String string = this.line;
        if (string == null) {
            LineInfo.$$$reportNull$$$0(6);
        }
        return string;
    }

    public int getNextLineIndent() {
        return this.nextLineIndent;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LineInfo)) {
            return false;
        }
        LineInfo other = (LineInfo)o;
        if (this.getLineCount() != other.getLineCount()) {
            return false;
        }
        if (this.getLineNumber() != other.getLineNumber()) {
            return false;
        }
        if (this.getLineStartOffset() != other.getLineStartOffset()) {
            return false;
        }
        if (this.getColumnOffset() != other.getColumnOffset()) {
            return false;
        }
        if (this.getNextLineIndent() != other.getNextLineIndent()) {
            return false;
        }
        String this$line = this.getLine();
        String other$line = other.getLine();
        return !(this$line == null ? other$line != null : !this$line.equals(other$line));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getLineCount();
        result = result * 59 + this.getLineNumber();
        result = result * 59 + this.getLineStartOffset();
        result = result * 59 + this.getColumnOffset();
        result = result * 59 + this.getNextLineIndent();
        String $line = this.getLine();
        result = result * 59 + ($line == null ? 43 : $line.hashCode());
        return result;
    }

    public String toString() {
        return "LineInfo(lineCount=" + this.getLineCount() + ", lineNumber=" + this.getLineNumber() + ", lineStartOffset=" + this.getLineStartOffset() + ", columnOffset=" + this.getColumnOffset() + ", line=" + this.getLine() + ", nextLineIndent=" + this.getNextLineIndent() + ")";
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
            case 1: 
            case 2: 
            case 3: 
            case 6: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 1: 
            case 2: 
            case 3: 
            case 6: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "document";
                break;
            }
            case 1: 
            case 2: 
            case 3: 
            case 6: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/request/LineInfo";
                break;
            }
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "line";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/request/LineInfo";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "getLinePrefix";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "getLineSuffix";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[1] = "getWhitespaceBeforeCursor";
                break;
            }
            case 6: {
                objectArray = objectArray2;
                objectArray2[1] = "getLine";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "create";
                break;
            }
            case 1: 
            case 2: 
            case 3: 
            case 6: {
                break;
            }
            case 4: {
                objectArray = objectArray;
                objectArray[2] = "calculateNextLineIndent";
                break;
            }
            case 5: {
                objectArray = objectArray;
                objectArray[2] = "<init>";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 1: 
            case 2: 
            case 3: 
            case 6: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

