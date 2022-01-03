/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.util.TextRange
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.completions;

import com.github.copilot.completions.CopilotCompletion;
import com.github.copilot.completions.CopilotEditorInlay;
import com.github.copilot.completions.CopilotInlayList;
import com.intellij.openapi.util.TextRange;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

class DefaultInlayList
implements CopilotInlayList {
    private final List<CopilotEditorInlay> inlays;
        private final CopilotCompletion copilotCompletion;
        private final TextRange replacementRange;
        private final String replacementText;

    DefaultInlayList(CopilotCompletion copilotCompletion, TextRange replacementRange, String replacementText, Collection<CopilotEditorInlay> inlays) {
        if (copilotCompletion == null) {
            throw new IllegalStateException("copilotCompletion cannot be null!");
        }
        if (replacementRange == null) {
            throw new IllegalStateException("replacementRange cannot be null!");
        }
        if (replacementText == null) {
            throw new IllegalStateException("replacementText cannot be null!");
        }
        if (inlays == null) {
            throw new IllegalStateException("inlays cannot be null!");
        }
        this.copilotCompletion = copilotCompletion;
        this.replacementRange = replacementRange;
        this.inlays = List.copyOf(inlays);
        this.replacementText = replacementText;
    }

    @Override
    public boolean isEmpty() {
        return this.inlays.isEmpty();
    }

    @Override
        public Iterator<CopilotEditorInlay> iterator() {
        Iterator<CopilotEditorInlay> iterator = this.inlays.iterator();
        if (iterator == null) {
            throw new IllegalStateException("iterator cannot be null!");
        }
        return iterator;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DefaultInlayList)) {
            return false;
        }
        DefaultInlayList other = (DefaultInlayList)o;
        if (!other.canEqual(this)) {
            return false;
        }
        List<CopilotEditorInlay> this$inlays = this.getInlays();
        List<CopilotEditorInlay> other$inlays = other.getInlays();
        if (this$inlays == null ? other$inlays != null : !((Object)this$inlays).equals(other$inlays)) {
            return false;
        }
        CopilotCompletion this$copilotCompletion = this.getCopilotCompletion();
        CopilotCompletion other$copilotCompletion = other.getCopilotCompletion();
        if (this$copilotCompletion == null ? other$copilotCompletion != null : !this$copilotCompletion.equals(other$copilotCompletion)) {
            return false;
        }
        TextRange this$replacementRange = this.getReplacementRange();
        TextRange other$replacementRange = other.getReplacementRange();
        if (this$replacementRange == null ? other$replacementRange != null : !this$replacementRange.equals(other$replacementRange)) {
            return false;
        }
        String this$replacementText = this.getReplacementText();
        String other$replacementText = other.getReplacementText();
        return !(this$replacementText == null ? other$replacementText != null : !this$replacementText.equals(other$replacementText));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DefaultInlayList;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<CopilotEditorInlay> $inlays = this.getInlays();
        result = result * 59 + ($inlays == null ? 43 : ((Object)$inlays).hashCode());
        CopilotCompletion $copilotCompletion = this.getCopilotCompletion();
        result = result * 59 + ($copilotCompletion == null ? 43 : $copilotCompletion.hashCode());
        TextRange $replacementRange = this.getReplacementRange();
        result = result * 59 + ($replacementRange == null ? 43 : $replacementRange.hashCode());
        String $replacementText = this.getReplacementText();
        result = result * 59 + ($replacementText == null ? 43 : $replacementText.hashCode());
        return result;
    }

    public String toString() {
        return "DefaultInlayList(inlays=" + this.getInlays() + ", copilotCompletion=" + this.getCopilotCompletion() + ", replacementRange=" + this.getReplacementRange() + ", replacementText=" + this.getReplacementText() + ")";
    }

    @Override
    public List<CopilotEditorInlay> getInlays() {
        return this.inlays;
    }

    @Override
        public CopilotCompletion getCopilotCompletion() {
        CopilotCompletion copilotCompletion = this.copilotCompletion;
        if (copilotCompletion == null) {
            throw new IllegalStateException("copilotCompletion cannot be null!");
        }
        return copilotCompletion;
    }

    @Override
        public TextRange getReplacementRange() {
        TextRange textRange = this.replacementRange;
        if (textRange == null) {
            throw new IllegalStateException("textRange cannot be null!");
        }
        return textRange;
    }

    @Override
        public String getReplacementText() {
        String string = this.replacementText;
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
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
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "copilotCompletion";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "replacementRange";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "replacementText";
                break;
            }
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "inlays";
                break;
            }
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/completions/DefaultInlayList";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/completions/DefaultInlayList";
                break;
            }
            case 4: {
                objectArray = objectArray2;
                objectArray2[1] = "iterator";
                break;
            }
            case 5: {
                objectArray = objectArray2;
                objectArray2[1] = "getCopilotCompletion";
                break;
            }
            case 6: {
                objectArray = objectArray2;
                objectArray2[1] = "getReplacementRange";
                break;
            }
            case 7: {
                objectArray = objectArray2;
                objectArray2[1] = "getReplacementText";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "<init>";
                break;
            }
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

