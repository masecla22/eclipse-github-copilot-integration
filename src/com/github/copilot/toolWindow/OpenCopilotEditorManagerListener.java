/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.fileEditor.FileEditor
 *  com.intellij.openapi.fileEditor.FileEditorManagerEvent
 *  com.intellij.openapi.fileEditor.FileEditorManagerListener
 *  com.intellij.openapi.fileEditor.TextEditor
 *  com.intellij.openapi.project.Project
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.toolWindow;

import com.github.copilot.toolWindow.OpenCopilotToolWindowFactory;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;

public class OpenCopilotEditorManagerListener
implements FileEditorManagerListener {
    private final Project project;

    public OpenCopilotEditorManagerListener(Project project) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        this.project = project;
    }

    public void selectionChanged(FileEditorManagerEvent event) {
        FileEditor newEditor;
        if (event == null) {
            throw new IllegalStateException("event cannot be null!");
        }
        if ((newEditor = event.getNewEditor()) instanceof TextEditor) {
            OpenCopilotToolWindowFactory.editorSelectionChanged(this.project, ((TextEditor)newEditor).getEditor());
        } else {
            OpenCopilotToolWindowFactory.editorSelectionChanged(this.project, null);
        }
    }

    
}

