/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.application.options.CodeStyle
 *  com.intellij.codeInsight.lookup.LookupManager
 *  com.intellij.codeInsight.template.TemplateManager
 *  com.intellij.openapi.actionSystem.AnActionEvent
 *  com.intellij.openapi.actionSystem.DataContext
 *  com.intellij.openapi.editor.Caret
 *  com.intellij.openapi.editor.Document
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.editor.actionSystem.EditorAction
 *  com.intellij.openapi.editor.actionSystem.EditorActionHandler
 *  com.intellij.openapi.project.DumbAware
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.util.TextRange
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.actions;

import com.github.copilot.actions.CopilotAction;
import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.util.EditorUtilCopy;
import com.intellij.application.options.CodeStyle;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import java.awt.event.KeyEvent;

public class CopilotApplyInlaysAction extends EditorAction implements DumbAware, CopilotAction {
	public static final String ID = "copilot.applyInlays";

	public CopilotApplyInlaysAction() {
		super((EditorActionHandler) new ApplyInlaysHandler());
		this.setInjectedContext(true);
	}

	public void update(AnActionEvent e) {
		if (e == null) {
			throw new IllegalStateException("e cannot be null!");
		}
		if (this.isIgnoredKeyboardEvent(e)) {
			e.getPresentation().setEnabled(false);
			return;
		}
		super.update(e);
	}

	private boolean isIgnoredKeyboardEvent(AnActionEvent e) {
		if (e == null) {
			throw new IllegalStateException("e cannot be null!");
		}
		if (!(e.getInputEvent() instanceof KeyEvent)) {
			return false;
		}
		if (((KeyEvent) e.getInputEvent()).getKeyChar() != '\t') {
			return false;
		}
		Project project = e.getProject();
		if (project == null) {
			return false;
		}
		Editor editor = this.getEditor(e.getDataContext());
		if (editor == null) {
			return false;
		}
		int blockIndent = CodeStyle.getIndentOptions((Project) project, (Document) editor.getDocument()).INDENT_SIZE;
		int caretOffset = editor.getCaretModel().getOffset();
		int line = editor.getDocument().getLineNumber(caretOffset);
		int caretAfterTab = EditorUtilCopy.indentLine(project, editor, line, blockIndent, caretOffset);
		TextRange tabRange = TextRange.from((int) caretOffset, (int) caretAfterTab);
		CopilotEditorManager editorManager = CopilotEditorManager.getInstance();
		return editorManager.countCompletionInlays(editor, tabRange, true, false, false, false) == 0
				&& editorManager.countCompletionInlays(editor, tabRange, false, true, true, false) == 0;
	}

	static boolean isSupported(Editor editor) {
		Project project;
		if (editor == null) {
			throw new IllegalStateException("editor cannot be null!");
		}
		return (project = editor.getProject()) != null && editor.getCaretModel().getCaretCount() == 1
				&& LookupManager.getActiveLookup((Editor) editor) == null
				&& CopilotEditorManager.getInstance().hasCompletionInlays(editor)
				&& TemplateManager.getInstance((Project) project).getActiveTemplate(editor) == null;
	}

	private static class ApplyInlaysHandler extends EditorActionHandler {
		private ApplyInlaysHandler() {
		}

		protected boolean isEnabledForCaret(Editor editor, Caret caret, DataContext dataContext) {
			if (editor == null) {
				throw new IllegalStateException("editor cannot be null!");
			}
			if (caret == null) {
				throw new IllegalStateException("caret cannot be null!");
			}
			return CopilotApplyInlaysAction.isSupported(editor);
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
			CopilotEditorManager.getInstance().applyCompletion(editor);
		}
	}
}
