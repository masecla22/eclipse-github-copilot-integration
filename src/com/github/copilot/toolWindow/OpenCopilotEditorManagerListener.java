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
import org.jetbrains.annotations.NotNull;

public class OpenCopilotEditorManagerListener
implements FileEditorManagerListener {
    private final Project project;

    public OpenCopilotEditorManagerListener(@NotNull Project project) {
        if (project == null) {
            OpenCopilotEditorManagerListener.$$$reportNull$$$0(0);
        }
        this.project = project;
    }

    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
        FileEditor newEditor;
        if (event == null) {
            OpenCopilotEditorManagerListener.$$$reportNull$$$0(1);
        }
        if ((newEditor = event.getNewEditor()) instanceof TextEditor) {
            OpenCopilotToolWindowFactory.editorSelectionChanged(this.project, ((TextEditor)newEditor).getEditor());
        } else {
            OpenCopilotToolWindowFactory.editorSelectionChanged(this.project, null);
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
        objectArray2[1] = "com/github/copilot/toolWindow/OpenCopilotEditorManagerListener";
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
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objectArray));
    }
}

