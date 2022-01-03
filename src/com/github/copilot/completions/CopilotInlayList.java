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

public interface CopilotInlayList
extends Iterable<CopilotEditorInlay> {
    public boolean isEmpty();

        public CopilotCompletion getCopilotCompletion();

        public TextRange getReplacementRange();

        public String getReplacementText();

        public List<CopilotEditorInlay> getInlays();
}

