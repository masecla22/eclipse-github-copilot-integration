/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.codeInsight.lookup.Lookup
 *  com.intellij.codeInsight.lookup.LookupManagerListener
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.psi.PsiFile
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.editor;

import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.editor.CopilotEditorUtil;
import com.github.copilot.editor.InlayDisposeContext;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.intellij.codeInsight.lookup.Lookup;
import com.intellij.codeInsight.lookup.LookupManagerListener;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;

public class CopilotLookupListener implements LookupManagerListener {
	private static final Logger LOG = Logger.getInstance(CopilotLookupListener.class);

	public void activeLookupChanged(Lookup oldLookup, Lookup newLookup) {
		PsiFile psiFile;
		LOG.debug("activeLookupChanged");
		Lookup validLookup = newLookup != null ? newLookup : oldLookup;
		PsiFile psiFile2 = psiFile = validLookup != null ? validLookup.getPsiFile() : null;
		if (psiFile != null && !CopilotApplicationSettings.isCopilotEnabled(psiFile)) {
			return;
		}
		CopilotEditorManager editorManager = CopilotEditorManager.getInstance();
		if (oldLookup != null && newLookup == null) {
			Editor editor;
			PsiFile file = oldLookup.getPsiFile();
			if (file != null && CopilotEditorUtil.isSelectedEditor(editor = oldLookup.getEditor())
					&& editorManager.isAvailable(editor) && !editor.getDocument().isInBulkUpdate()) {
				editorManager.editorModified(editor, editor.getCaretModel().getOffset(), true);
			}
		} else if (newLookup != null && oldLookup == null
				&& !CopilotApplicationSettings.settings().isShowIdeCompletions()) {
			Editor editor = newLookup.getEditor();
			editorManager.cancelCompletionRequests(editor);
			editorManager.disposeInlays(editor, InlayDisposeContext.IdeCompletion);
		}
	}
}
