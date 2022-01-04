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

public interface CopilotInlayRenderer extends EditorCustomElementRenderer {
	public List<String> getContentLines();

	public Inlay<CopilotInlayRenderer> getInlay();

	public CopilotCompletionType getType();
}
