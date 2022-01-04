/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.completions;

import java.util.List;

public interface CopilotEditorInlay {
	public CopilotCompletionType getType();

	public List<String> getLines();

	public int getEditorOffset();

	default public boolean isEmptyCompletion() {
		List<String> completion = this.getLines();
		return completion.isEmpty() || completion.size() == 1 && completion.get(0).isEmpty();
	}
}
