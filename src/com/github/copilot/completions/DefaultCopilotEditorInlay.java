/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.concurrent.Immutable
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.completions;

import com.github.copilot.completions.CopilotCompletionType;
import com.github.copilot.completions.CopilotEditorInlay;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import org.jetbrains.annotations.NotNull;

@Immutable
final class DefaultCopilotEditorInlay
implements CopilotEditorInlay {
        private final CopilotCompletionType type;
    private final int editorOffset;
        private final List<String> completionLines;

    public DefaultCopilotEditorInlay(CopilotCompletionType type, int editorOffset, List<String> completionLines) {
        if (type == null) {
            throw new IllegalStateException("type cannot be null!");
        }
        if (completionLines == null) {
            throw new IllegalStateException("completionLines cannot be null!");
        }
        this.type = type;
        this.editorOffset = editorOffset;
        this.completionLines = completionLines;
    }

    @Override
        public CopilotCompletionType getType() {
        CopilotCompletionType copilotCompletionType = this.type;
        if (copilotCompletionType == null) {
            throw new IllegalStateException("copilotCompletionType cannot be null!");
        }
        return copilotCompletionType;
    }

    @Override
        public List<String> getLines() {
        List<String> list = this.completionLines;
        if (list == null) {
            throw new IllegalStateException("list cannot be null!");
        }
        return list;
    }

    @Override
    public int getEditorOffset() {
        return this.editorOffset;
    }

        public List<String> getCompletionLines() {
        List<String> list = this.completionLines;
        if (list == null) {
            throw new IllegalStateException("list cannot be null!");
        }
        return list;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DefaultCopilotEditorInlay)) {
            return false;
        }
        DefaultCopilotEditorInlay other = (DefaultCopilotEditorInlay)o;
        if (this.getEditorOffset() != other.getEditorOffset()) {
            return false;
        }
        CopilotCompletionType this$type = this.getType();
        CopilotCompletionType other$type = other.getType();
        if (this$type == null ? other$type != null : !((Object)((Object)this$type)).equals((Object)other$type)) {
            return false;
        }
        List<String> this$completionLines = this.getCompletionLines();
        List<String> other$completionLines = other.getCompletionLines();
        return !(this$completionLines == null ? other$completionLines != null : !((Object)this$completionLines).equals(other$completionLines));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getEditorOffset();
        CopilotCompletionType $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)((Object)$type)).hashCode());
        List<String> $completionLines = this.getCompletionLines();
        result = result * 59 + ($completionLines == null ? 43 : ((Object)$completionLines).hashCode());
        return result;
    }

    public String toString() {
        return "DefaultCopilotEditorInlay(type=" + this.getType() + ", editorOffset=" + this.getEditorOffset() + ", completionLines=" + this.getCompletionLines() + ")";
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
            case 3: 
            case 4: {
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
            case 3: 
            case 4: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "type";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "completionLines";
                break;
            }
            case 2: 
            case 3: 
            case 4: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/completions/DefaultCopilotEditorInlay";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/completions/DefaultCopilotEditorInlay";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "getType";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[1] = "getLines";
                break;
            }
            case 4: {
                objectArray = objectArray2;
                objectArray2[1] = "getCompletionLines";
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
            case 3: 
            case 4: {
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
            case 3: 
            case 4: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

