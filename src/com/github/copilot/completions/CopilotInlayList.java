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
import com.intellij.openapi.util.TextRange;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface CopilotInlayList
extends Iterable<CopilotEditorInlay> {
    public boolean isEmpty();

    @NotNull
    public CopilotCompletion getCopilotCompletion();

    @NotNull
    public TextRange getReplacementRange();

    @NotNull
    public String getReplacementText();

    @NotNull
    public List<CopilotEditorInlay> getInlays();
}

