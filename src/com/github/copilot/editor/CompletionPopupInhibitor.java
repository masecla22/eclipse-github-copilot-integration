/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.codeInsight.editorActions.TypedHandlerDelegate
 *  com.intellij.codeInsight.editorActions.TypedHandlerDelegate$Result
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.project.Project
 *  com.intellij.psi.PsiFile
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.editor;

import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class CompletionPopupInhibitor
extends TypedHandlerDelegate {
    private static final Logger LOG = Logger.getInstance(CompletionPopupInhibitor.class);

        public TypedHandlerDelegate.Result checkAutoPopup(char charTyped, Project project, Editor editor, PsiFile file) {
        boolean showIdeCompletions;
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (file == null) {
            throw new IllegalStateException("file cannot be null!");
        }
        if (!(showIdeCompletions = CopilotApplicationSettings.settings().isShowIdeCompletions()) && CopilotEditorManager.getInstance().hasTypingAsSuggestedData(editor, charTyped)) {
            LOG.debug("inhibiting IDE completion popup because typing-as-suggested is available");
            TypedHandlerDelegate.Result result = TypedHandlerDelegate.Result.STOP;
            if (result == null) {
                throw new IllegalStateException("result cannot be null!");
            }
            return result;
        }
        TypedHandlerDelegate.Result result = super.checkAutoPopup(charTyped, project, editor, file);
        if (result == null) {
            throw new IllegalStateException("result cannot be null!");
        }
        return result;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
            case 3: 
            case 4: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 3: 
            case 4: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "project";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "editor";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "file";
                break;
            }
            case 3: 
            case 4: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/editor/CompletionPopupInhibitor";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/editor/CompletionPopupInhibitor";
                break;
            }
            case 3: 
            case 4: {
                objectArray = objectArray2;
                objectArray2[1] = "checkAutoPopup";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "checkAutoPopup";
                break;
            }
            case 3: 
            case 4: {
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 3: 
            case 4: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

