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
import org.jetbrains.annotations.NotNull;

public class EditorSelectionListener
implements FileEditorManagerListener {
    private final Project project;

    public EditorSelectionListener(Project project) {
        if (project == null) {
            EditorSelectionListener.$$$reportNull$$$0(0);
        }
        this.project = project;
    }

    public void selectionChanged(FileEditorManagerEvent event) {
        VirtualFile oldFile;
        if (event == null) {
            EditorSelectionListener.$$$reportNull$$$0(1);
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

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2;
        Object[] objectArray3 = new Object[3];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "project";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "event";
                break;
            }
        }
        objectArray2[1] = "com/github/copilot/editor/EditorSelectionListener";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "<init>";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[2] = "selectionChanged";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

