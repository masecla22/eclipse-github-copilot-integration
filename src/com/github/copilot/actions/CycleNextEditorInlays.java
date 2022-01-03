/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.actionSystem.AnActionEvent
 *  com.intellij.openapi.actionSystem.CommonDataKeys
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.project.DumbAware
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.actions;

import com.github.copilot.actions.CopilotAction;
import com.github.copilot.editor.CopilotEditorManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

public class CycleNextEditorInlays
extends AnAction
implements DumbAware,
CopilotAction {
    public void update(AnActionEvent e) {
        if (e == null) {
            throw new IllegalStateException("e cannot be null!");
        }
        Editor editor = (Editor)e.getData(CommonDataKeys.EDITOR);
        e.getPresentation().setEnabled(editor != null && CopilotEditorManager.getInstance().hasNextInlaySet(editor));
    }

    public void actionPerformed(AnActionEvent e) {
        Editor editor;
        if (e == null) {
            throw new IllegalStateException("e cannot be null!");
        }
        if ((editor = (Editor)e.getData(CommonDataKeys.EDITOR)) != null) {
            CopilotEditorManager.getInstance().showNextInlaySet(editor);
        }
    }
}

