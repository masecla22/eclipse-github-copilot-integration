/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.codeInsight.lookup.LookupManager
 *  com.intellij.openapi.actionSystem.DataContext
 *  com.intellij.openapi.editor.Caret
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.editor.actionSystem.EditorAction
 *  com.intellij.openapi.editor.actionSystem.EditorActionHandler
 *  com.intellij.openapi.project.DumbAware
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.actions;

import com.github.copilot.actions.CopilotAction;
import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.editor.InlayDisposeContext;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CopilotDisposeInlaysAction
extends EditorAction
implements DumbAware,
CopilotAction {
    public CopilotDisposeInlaysAction() {
        super((EditorActionHandler)new DisposeInlaysHandler());
        this.setInjectedContext(true);
    }

    static boolean isSupported(Editor editor) {
        CopilotEditorManager manager;
        if (editor == null) {
            CopilotDisposeInlaysAction.$$$reportNull$$$0(0);
        }
        return (manager = CopilotEditorManager.getInstance()).isAvailable(editor) && manager.hasCompletionInlays(editor) && LookupManager.getActiveLookup((Editor)editor) == null;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", "editor", "com/github/copilot/actions/CopilotDisposeInlaysAction", "isSupported"));
    }

    private static class DisposeInlaysHandler
    extends EditorActionHandler {
        private DisposeInlaysHandler() {
        }

        protected boolean isEnabledForCaret(Editor editor, Caret caret, DataContext dataContext) {
            if (editor == null) {
                DisposeInlaysHandler.$$$reportNull$$$0(0);
            }
            if (caret == null) {
                DisposeInlaysHandler.$$$reportNull$$$0(1);
            }
            return CopilotDisposeInlaysAction.isSupported(editor);
        }

        public boolean executeInCommand(Editor editor, DataContext dataContext) {
            if (editor == null) {
                DisposeInlaysHandler.$$$reportNull$$$0(2);
            }
            return false;
        }

        protected void doExecute(Editor editor, Caret caret, DataContext dataContext) {
            if (editor == null) {
                DisposeInlaysHandler.$$$reportNull$$$0(3);
            }
            if (LookupManager.getActiveLookup((Editor)editor) == null) {
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
                    objectArray3[0] = "editor";
                    break;
                }
                case 1: {
                    objectArray2 = objectArray3;
                    objectArray3[0] = "caret";
                    break;
                }
            }
            objectArray2[1] = "com/github/copilot/actions/CopilotDisposeInlaysAction$DisposeInlaysHandler";
            switch (n) {
                default: {
                    objectArray = objectArray2;
                    objectArray2[2] = "isEnabledForCaret";
                    break;
                }
                case 2: {
                    objectArray = objectArray2;
                    objectArray2[2] = "executeInCommand";
                    break;
                }
                case 3: {
                    objectArray = objectArray2;
                    objectArray2[2] = "doExecute";
                    break;
                }
            }
            throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
        }
    }
}

