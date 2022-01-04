/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.codeInsight.lookup.LookupManager
 *  com.intellij.openapi.actionSystem.DataContext
 *  com.intellij.openapi.editor.Caret
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.editor.actionSystem.EditorAction
 *  com.intellij.openapi.editor.actionSystem.EditorActionHandler
 *  com.intellij.openapi.project.DumbAware
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.actions;

import com.github.copilot.actions.CopilotAction;
import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.editor.InlayDisposeContext;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.DumbAware;

public class CopilotDisposeInlaysAction extends EditorAction implements DumbAware, CopilotAction {
	public CopilotDisposeInlaysAction() {
		super((EditorActionHandler) new DisposeInlaysHandler());
		this.setInjectedContext(true);
	}

	static boolean isSupported(Editor editor) {
		CopilotEditorManager manager;
		if (editor == null) {
			throw new IllegalStateException("editor cannot be null!");
		}
		return (manager = CopilotEditorManager.getInstance()).isAvailable(editor) && manager.hasCompletionInlays(editor)
				&& LookupManager.getActiveLookup((Editor) editor) == null;
	}

	private static class DisposeInlaysHandler extends EditorActionHandler {
		private DisposeInlaysHandler() {
		}

		protected boolean isEnabledForCaret(Editor editor, Caret caret, DataContext dataContext) {
			if (editor == null) {
				throw new IllegalStateException("editor cannot be null!");
			}
			if (caret == null) {
				throw new IllegalStateException("caret cannot be null!");
			}
			return CopilotDisposeInlaysAction.isSupported(editor);
		}

		public boolean executeInCommand(Editor editor, DataContext dataContext) {
			if (editor == null) {
				throw new IllegalStateException("editor cannot be null!");
			}
			return false;
		}

		protected void doExecute(Editor editor, Caret caret, DataContext dataContext) {
			if (editor == null) {
				throw new IllegalStateException("editor cannot be null!");
			}
			if (LookupManager.getActiveLookup((Editor) editor) == null) {
				CopilotEditorManager.getInstance().disposeInlays(editor, InlayDisposeContext.UserAction);
			}
		}
	}
}
