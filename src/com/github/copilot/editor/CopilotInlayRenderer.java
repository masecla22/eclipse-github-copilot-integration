/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.editor.EditorCustomElementRenderer
 *  com.intellij.openapi.editor.Inlay
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.editor;

import com.github.copilot.completions.CopilotCompletionType;
import com.intellij.openapi.editor.EditorCustomElementRenderer;
import com.intellij.openapi.editor.Inlay;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface CopilotInlayRenderer
extends EditorCustomElementRenderer {
    @NotNull
    public List<String> getContentLines();

    @NotNull
    public Inlay<CopilotInlayRenderer> getInlay();

    @NotNull
    public CopilotCompletionType getType();
}

