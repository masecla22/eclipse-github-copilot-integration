/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.codeInsight.editorActions.TypedHandlerDelegate
 *  com.intellij.codeInsight.editorActions.TypedHandlerDelegate$Result
 *  me.masecla.copilot.extra.Logger
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.project.Project
 *  com.intellij.psi.PsiFile
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.editor;

import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import me.masecla.copilot.extra.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

public class CompletionPopupInhibitor extends TypedHandlerDelegate {
	private static final Logger LOG = Logger.getInstance(CompletionPopupInhibitor.class);

	public TypedHandlerDelegate.Result checkAutoPopup(char charTyped, Project project, Editor editor, PsiFile file) {
		boolean showIdeCompletions;
		if (project == null) {
			throw new IllegalStateException("project cannot be null!");
		}
		if (editor == null) {
			throw new IllegalStateException("editor cannot be null!");
		}
		if (file == null) {
			throw new IllegalStateException("file cannot be null!");
		}
		if (!(showIdeCompletions = CopilotApplicationSettings.settings().isShowIdeCompletions())
				&& CopilotEditorManager.getInstance().hasTypingAsSuggestedData(editor, charTyped)) {
			LOG.debug("inhibiting IDE completion popup because typing-as-suggested is available");
			TypedHandlerDelegate.Result result = TypedHandlerDelegate.Result.STOP;
			if (result == null) {
				throw new IllegalStateException("result cannot be null!");
			}
			return result;
		}
		TypedHandlerDelegate.Result result = super.checkAutoPopup(charTyped, project, editor, file);
		if (result == null) {
			throw new IllegalStateException("result cannot be null!");
		}
		return result;
	}

}
