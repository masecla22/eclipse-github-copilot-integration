/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.util.TextRange
 *  com.intellij.util.concurrency.annotations.RequiresEdt
 *  com.intellij.util.messages.Topic
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.editor;

import com.github.copilot.completions.CopilotInlayList;
import com.github.copilot.editor.CopilotInlayRenderer;
import com.github.copilot.editor.InlayDisposeContext;
import com.github.copilot.editor.InlayMessage;
import com.github.copilot.request.EditorRequest;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.concurrency.annotations.RequiresEdt;
import com.intellij.util.messages.Topic;
import java.util.List;

public interface CopilotEditorManager extends Disposable {
	public static final Topic<InlayMessage> INLAY_TOPIC = Topic.create((String) "copilot.inlayUpdate",
			InlayMessage.class);

	public static CopilotEditorManager getInstance() {
		CopilotEditorManager copilotEditorManager = (CopilotEditorManager) ApplicationManager.getApplication()
				.getService(CopilotEditorManager.class);
		if (copilotEditorManager == null) {
			throw new IllegalStateException("copilotEditorManager cannot be null!");
		}
		return copilotEditorManager;
	}

	@RequiresEdt
	public boolean isAvailable(Editor var1);

	@RequiresEdt
	default public boolean hasCompletionInlays(Editor editor) {
		if (editor == null) {
			throw new IllegalStateException("editor cannot be null!");
		}
		return this.countCompletionInlays(editor, TextRange.from((int) 0, (int) editor.getDocument().getTextLength()),
				true, true, true, true) > 0;
	}

	@RequiresEdt
	public int countCompletionInlays(Editor var1, TextRange var2, boolean var3, boolean var4, boolean var5,
			boolean var6);

	@RequiresEdt
	public boolean hasTypingAsSuggestedData(Editor var1, char var2);

	@RequiresEdt
	public boolean applyCompletion(Editor var1);

	@RequiresEdt
	public void applyCompletion(Project var1, Editor var2, EditorRequest var3, CopilotInlayList var4);

	@RequiresEdt
	public void disposeInlays(Editor var1, InlayDisposeContext var2);

	public void editorModified(Editor var1);

	default public void editorModified(Editor editor, boolean force) {
		if (editor == null) {
			throw new IllegalStateException("editor cannot be null!");
		}
		this.editorModified(editor, editor.getCaretModel().getOffset(), force);
	}

	@RequiresEdt
	default public void editorModified(Editor editor, int offset) {
		if (editor == null) {
			throw new IllegalStateException("editor cannot be null!");
		}
		this.editorModified(editor, offset, false);
	}

	@RequiresEdt
	public void editorModified(Editor var1, int var2, boolean var3);

	@RequiresEdt
	public void cancelCompletionRequests(Editor var1);

	@RequiresEdt
	public List<CopilotInlayRenderer> collectInlays(Editor var1, int var2, int var3);

	public boolean hasNextInlaySet(Editor var1);

	public boolean hasPreviousInlaySet(Editor var1);

	public void showNextInlaySet(Editor var1);

	public void showPreviousInlaySet(Editor var1);

}
