/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.icons.AllIcons$Actions
 *  com.intellij.openapi.actionSystem.ActionManager
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.actionSystem.AnActionEvent
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.fileEditor.FileEditorManager
 *  com.intellij.openapi.project.Project
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.actions;

import com.github.copilot.CopilotBundle;
import com.github.copilot.toolWindow.CopilotSplitEditorManager;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;

public class RefreshCopilotToolWindowAction
extends AnAction {
    public RefreshCopilotToolWindowAction() {
        super(CopilotBundle.get("openCopilot.refreshAction"), null, AllIcons.Actions.Refresh);
        AnAction refreshAction = ActionManager.getInstance().getAction("Refresh");
        if (refreshAction != null) {
            this.copyShortcutFrom(refreshAction);
        }
    }

    public void update(AnActionEvent e) {
        if (e == null) {
            throw new IllegalStateException("e cannot be null!");
        }
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }

    public void actionPerformed(AnActionEvent e) {
        Project project;
        if (e == null) {
            throw new IllegalStateException("e cannot be null!");
        }
        if ((project = e.getProject()) == null) {
            return;
        }
        Editor editor = FileEditorManager.getInstance((Project)project).getSelectedTextEditor();
        if (editor != null) {
            CopilotSplitEditorManager.getInstance().openCopilot(editor, true);
        }
    }
}

