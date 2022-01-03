/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.actionSystem.ActionPromoter
 *  com.intellij.openapi.actionSystem.ActionWithDelegate
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.actionSystem.CommonDataKeys
 *  com.intellij.openapi.actionSystem.DataContext
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.editor.actionSystem.EditorAction
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.actions;

import com.github.copilot.actions.CopilotAction;
import com.github.copilot.actions.CopilotApplyInlaysAction;
import com.github.copilot.actions.CopilotDisposeInlaysAction;
import com.intellij.openapi.actionSystem.ActionPromoter;
import com.intellij.openapi.actionSystem.ActionWithDelegate;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CopilotActionPromoter
implements ActionPromoter {
    public List<AnAction> promote(List<? extends AnAction> actions, DataContext context) {
        Editor editor;
        if (actions == null) {
            CopilotActionPromoter.$$$reportNull$$$0(0);
        }
        if (context == null) {
            CopilotActionPromoter.$$$reportNull$$$0(1);
        }
        if ((editor = (Editor)CommonDataKeys.EDITOR.getData(context)) == null || !CopilotApplyInlaysAction.isSupported(editor) && !CopilotDisposeInlaysAction.isSupported(editor)) {
            return null;
        }
        if (actions.stream().noneMatch(action -> action instanceof CopilotAction && action instanceof EditorAction)) {
            return null;
        }
        ArrayList<AnAction> result = new ArrayList<AnAction>(actions);
        result.sort((a, b) -> {
            boolean bIsCopilot;
            boolean aIsCopilot = a instanceof CopilotAction && a instanceof EditorAction;
            boolean bl = bIsCopilot = b instanceof CopilotAction && b instanceof EditorAction;
            if (aIsCopilot && bIsCopilot) {
                if (a instanceof CopilotApplyInlaysAction) {
                    return -1;
                }
                if (b instanceof CopilotApplyInlaysAction) {
                    return -1;
                }
            }
            if (CopilotActionPromoter.isIdeaVimAction(a) || CopilotActionPromoter.isIdeaVimAction(b)) {
                return 0;
            }
            if (aIsCopilot) {
                return -1;
            }
            if (bIsCopilot) {
                return 1;
            }
            return 0;
        });
        return result;
    }

    private static boolean isIdeaVimAction(AnAction action) {
        if (action == null) {
            CopilotActionPromoter.$$$reportNull$$$0(2);
        }
        String packagePrefix = "com.maddyhome.idea.vim";
        if (action.getClass().getName().startsWith(packagePrefix)) {
            return true;
        }
        if (action instanceof ActionWithDelegate) {
            Object delegate = ((ActionWithDelegate)action).getDelegate();
            return delegate.getClass().getName().startsWith(packagePrefix);
        }
        return false;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2;
        Object[] objectArray3 = new Object[3];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "actions";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "context";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "action";
                break;
            }
        }
        objectArray2[1] = "com/github/copilot/actions/CopilotActionPromoter";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "promote";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[2] = "isIdeaVimAction";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

