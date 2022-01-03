/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.actionSystem.AnActionEvent
 *  com.intellij.openapi.actionSystem.CommonDataKeys
 *  com.intellij.openapi.editor.Editor
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.actions;

import com.github.copilot.actions.CopilotAction;
import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.toolWindow.CopilotSplitEditorManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;

public class OpenCopilotAction
extends AnAction
implements CopilotAction {
    public void update(AnActionEvent e) {
        if (e == null) {
            throw new IllegalStateException("e cannot be null!");
        }
        Editor editor = (Editor)e.getData(CommonDataKeys.EDITOR);
        e.getPresentation().setEnabled(editor != null && CopilotEditorManager.getInstance().isAvailable(editor));
    }

    public boolean isDumbAware() {
        return true;
    }

    public void actionPerformed(AnActionEvent e) {
        Editor editor;
        if (e == null) {
            throw new IllegalStateException("e cannot be null!");
        }
        if ((editor = (Editor)e.getData(CommonDataKeys.EDITOR)) == null || !CopilotEditorManager.getInstance().isAvailable(editor)) {
            return;
        }
        CopilotSplitEditorManager.getInstance().openCopilot(editor, true);
    }
}

