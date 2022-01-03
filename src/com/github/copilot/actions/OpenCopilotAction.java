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

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        objectArray2[0] = "e";
        objectArray2[1] = "com/github/copilot/actions/OpenCopilotAction";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "update";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[2] = "actionPerformed";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

