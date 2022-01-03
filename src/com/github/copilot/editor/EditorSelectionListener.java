/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.fileEditor.FileEditor
 *  com.intellij.openapi.fileEditor.FileEditorManagerEvent
 *  com.intellij.openapi.fileEditor.FileEditorManagerListener
 *  com.intellij.openapi.fileEditor.TextEditor
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.vfs.VirtualFile
 *  com.intellij.psi.PsiFile
 *  com.intellij.psi.PsiManager
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.editor;

import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.editor.InlayDisposeContext;
import com.github.copilot.lang.LanguageSupport;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

public class EditorSelectionListener
implements FileEditorManagerListener {
    private final Project project;

    public EditorSelectionListener(Project project) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        this.project = project;
    }

    public void selectionChanged(FileEditorManagerEvent event) {
        VirtualFile oldFile;
        if (event == null) {
            throw new IllegalStateException("event cannot be null!");
        }
        if ((oldFile = event.getOldFile()) == null || !oldFile.isValid()) {
            return;
        }
        PsiFile psiFile = PsiManager.getInstance((Project)this.project).findFile(oldFile);
        if (psiFile == null || !psiFile.isValid()) {
            return;
        }
        if (LanguageSupport.find(psiFile) == null) {
            return;
        }
        FileEditor oldEditor = event.getOldEditor();
        if (oldEditor instanceof TextEditor) {
            Editor editor = ((TextEditor)oldEditor).getEditor();
            CopilotEditorManager.getInstance().disposeInlays(editor, InlayDisposeContext.UserAction);
        }
    }

    
}

