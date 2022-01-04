/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.editor.Document
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.util.ThrowableRunnable
 *  com.intellij.util.concurrency.annotations.RequiresEdt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.TestOnly
 */
package com.github.copilot.editor;

import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.editor.CopilotEditorManagerImpl;
import com.github.copilot.editor.CopilotInlayRenderer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.util.ThrowableRunnable;
import com.intellij.util.concurrency.annotations.RequiresEdt;
import java.util.List;
import org.jetbrains.annotations.TestOnly;

@TestOnly
public class CopilotEditorTestManager extends CopilotEditorManagerImpl {
	private volatile boolean disabled;

	public static CopilotEditorTestManager getInstance() {
		CopilotEditorTestManager copilotEditorTestManager = (CopilotEditorTestManager) ApplicationManager
				.getApplication().getService(CopilotEditorManager.class);
		if (copilotEditorTestManager == null) {
			throw new IllegalStateException("copilotEditorTestManager cannot be null!");
		}
		return copilotEditorTestManager;
	}

	@RequiresEdt
	public List<CopilotInlayRenderer> collectInlays(Editor editor) {
		if (editor == null) {
			throw new IllegalStateException("editor cannot be null!");
		}
		Document document = editor.getDocument();
		List<CopilotInlayRenderer> list = this.collectInlays(editor, 0, document.getTextLength());
		if (list == null) {
			throw new IllegalStateException("list cannot be null!");
		}
		return list;
	}

	@RequiresEdt
	public List<CopilotInlayRenderer> collectCurrentLineInlays(Editor editor) {
		if (editor == null) {
			throw new IllegalStateException("editor cannot be null!");
		}
		Document document = editor.getDocument();
		int line = document.getLineNumber(editor.getCaretModel().getOffset());
		int lineStart = document.getLineStartOffset(line);
		int lineEnd = document.getLineEndOffset(line);
		List<CopilotInlayRenderer> list = this.collectInlays(editor, lineStart, lineEnd);
		if (list == null) {
			throw new IllegalStateException("list cannot be null!");
		}
		return list;
	}

	public void withDisabled(ThrowableRunnable<Exception> action) throws Exception {
		if (action == null) {
			throw new IllegalStateException("action cannot be null!");
		}
		try {
			this.disabled = true;
			action.run();
		} finally {
			this.disabled = false;
		}
	}

	@Override
	public boolean isAvailable(Editor editor) {
		if (editor == null) {
			throw new IllegalStateException("editor cannot be null!");
		}
		return !this.disabled && super.isAvailable(editor);
	}

}
