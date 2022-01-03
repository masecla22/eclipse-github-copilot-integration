/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.codeInsight.lookup.LookupManager
 *  com.intellij.openapi.actionSystem.DataContext
 *  com.intellij.openapi.editor.Caret
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.editor.actionSystem.EditorActionHandler
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.editor;

import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.editor.InlayDisposeContext;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CopilotDisposeInlaysEditorHandler
extends EditorActionHandler {
        private final EditorActionHandler baseHandler;

    public CopilotDisposeInlaysEditorHandler(EditorActionHandler baseHandler) {
        this.baseHandler = baseHandler;
    }

    protected boolean isEnabledForCaret(Editor editor, Caret caret, DataContext dataContext) {
        CopilotEditorManager manager;
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (caret == null) {
            throw new IllegalStateException("caret cannot be null!");
        }
        return (manager = CopilotEditorManager.getInstance()).isAvailable(editor) && manager.hasCompletionInlays(editor) && LookupManager.getActiveLookup((Editor)editor) == null || this.baseHandler != null && this.baseHandler.isEnabled(editor, caret, dataContext);
    }

    public boolean executeInCommand(Editor editor, DataContext dataContext) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        return this.baseHandler != null && this.baseHandler.executeInCommand(editor, dataContext);
    }

    protected void doExecute(Editor editor, Caret caret, DataContext dataContext) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (LookupManager.getActiveLookup((Editor)editor) == null) {
            CopilotEditorManager.getInstance().disposeInlays(editor, InlayDisposeContext.CaretChange);
        }
        if (this.baseHandler != null && this.baseHandler.isEnabled(editor, caret, dataContext)) {
            this.baseHandler.execute(editor, caret, dataContext);
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
        objectArray2[1] = "com/github/copilot/editor/CopilotDisposeInlaysEditorHandler";
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

