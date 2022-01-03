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

public class CyclePreviousEditorInlays
extends AnAction
implements DumbAware,
CopilotAction {
    public void update(AnActionEvent e) {
        if (e == null) {
            CyclePreviousEditorInlays.$$$reportNull$$$0(0);
        }
        Editor editor = (Editor)e.getData(CommonDataKeys.EDITOR);
        e.getPresentation().setEnabled(editor != null && CopilotEditorManager.getInstance().hasPreviousInlaySet(editor));
    }

    public void actionPerformed(AnActionEvent e) {
        Editor editor;
        if (e == null) {
            CyclePreviousEditorInlays.$$$reportNull$$$0(1);
        }
        if ((editor = (Editor)e.getData(CommonDataKeys.EDITOR)) != null) {
            CopilotEditorManager.getInstance().showPreviousInlaySet(editor);
        }
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        objectArray2[0] = "e";
        objectArray2[1] = "com/github/copilot/actions/CyclePreviousEditorInlays";
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
