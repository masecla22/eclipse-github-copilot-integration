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
import org.jetbrains.annotations.NotNull;

public class CopilotSplitEditorManager {
    private static final Logger LOG = Logger.getInstance(CopilotSplitEditorManager.class);

        public static CopilotSplitEditorManager getInstance() {
        CopilotSplitEditorManager copilotSplitEditorManager = (CopilotSplitEditorManager)ApplicationManager.getApplication().getService(CopilotSplitEditorManager.class);
        if (copilotSplitEditorManager == null) {
            CopilotSplitEditorManager.$$$reportNull$$$0(0);
        }
        return copilotSplitEditorManager;
    }

    @RequiresEdt
    public void openCopilot(Editor editor, boolean force) {
        OpenCopilotHandler handler;
        if (editor == null) {
            CopilotSplitEditorManager.$$$reportNull$$$0(1);
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

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "method %s.%s must not return null";
                break;
            }
            case 1: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/toolWindow/CopilotSplitEditorManager";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "editor";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getInstance";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/toolWindow/CopilotSplitEditorManager";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: {
                objectArray = objectArray;
                objectArray[2] = "openCopilot";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
            case 1: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

