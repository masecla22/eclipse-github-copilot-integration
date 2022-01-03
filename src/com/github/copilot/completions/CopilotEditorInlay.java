/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.completions;

import com.github.copilot.completions.CopilotCompletionType;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface CopilotEditorInlay {
    @NotNull
    public CopilotCompletionType getType();

    @NotNull
    public List<String> getLines();

    public int getEditorOffset();

    default public boolean isEmptyCompletion() {
        List<String> completion = this.getLines();
        return completion.isEmpty() || completion.size() == 1 && completion.get(0).isEmpty();
    }
}

