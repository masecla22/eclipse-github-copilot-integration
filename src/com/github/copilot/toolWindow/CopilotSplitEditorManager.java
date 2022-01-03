/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.editor.Document
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.wm.ToolWindow
 *  com.intellij.openapi.wm.ToolWindowManager
 *  com.intellij.psi.PsiDocumentManager
 *  com.intellij.psi.PsiFile
 *  com.intellij.util.concurrency.annotations.RequiresEdt
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.toolWindow;

import com.github.copilot.completions.CopilotCompletionService;
import com.github.copilot.editor.CopilotEditorUtil;
import com.github.copilot.lang.LanguageSupport;
import com.github.copilot.request.CompletionType;
import com.github.copilot.request.EditorRequest;
import com.github.copilot.toolWindow.OpenCopilotHandler;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.concurrency.annotations.RequiresEdt;

public class CopilotSplitEditorManager {
    private static final Logger LOG = Logger.getInstance(CopilotSplitEditorManager.class);

        public static CopilotSplitEditorManager getInstance() {
        CopilotSplitEditorManager copilotSplitEditorManager = (CopilotSplitEditorManager)ApplicationManager.getApplication().getService(CopilotSplitEditorManager.class);
        if (copilotSplitEditorManager == null) {
            throw new IllegalStateException("copilotSplitEditorManager cannot be null!");
        }
        return copilotSplitEditorManager;
    }

    @RequiresEdt
    public void openCopilot(Editor editor, boolean force) {
        OpenCopilotHandler handler;
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        LOG.debug("openCopilot");
        if (!force && !CopilotEditorUtil.isFocusedEditor(editor)) {
            LOG.debug("skipping completions for unfocused editor: " + editor);
            return;
        }
        Project project = editor.getProject();
        if (project == null) {
            return;
        }
        Document document = editor.getDocument();
        PsiFile psiFile = PsiDocumentManager.getInstance((Project)project).getPsiFile(document);
        if (psiFile == null) {
            return;
        }
        LanguageSupport language = LanguageSupport.find(psiFile);
        if (language == null) {
            return;
        }
        int offset = editor.getCaretModel().getOffset();
        int eolOffset = editor.getDocument().getLineEndOffset(editor.getDocument().getLineNumber(offset));
        EditorRequest request = CopilotCompletionService.getInstance().createRequest(editor, eolOffset, CompletionType.OpenCopilot);
        if (request == null) {
            return;
        }
        CopilotEditorUtil.addEditorRequest(editor, request);
        ToolWindow toolWindow = ToolWindowManager.getInstance((Project)project).getToolWindow("github.copilotToolWindow");
        if (toolWindow != null) {
            toolWindow.getContentManager().removeAllContents(true);
            toolWindow.show();
        }
        if ((handler = OpenCopilotHandler.create(project, psiFile.getFileType(), editor, request)) != null) {
            handler.updateToolWindow();
        }
    }

    
}

