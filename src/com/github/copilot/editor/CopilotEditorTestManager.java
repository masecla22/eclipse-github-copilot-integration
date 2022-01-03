/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.editor.Document
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.util.ThrowableRunnable
 *  com.intellij.util.concurrency.annotations.RequiresEdt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.TestOnly
 */
package com.github.copilot.editor;

import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.editor.CopilotEditorManagerImpl;
import com.github.copilot.editor.CopilotInlayRenderer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.util.ThrowableRunnable;
import com.intellij.util.concurrency.annotations.RequiresEdt;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

@TestOnly
public class CopilotEditorTestManager
extends CopilotEditorManagerImpl {
    private volatile boolean disabled;

        public static CopilotEditorTestManager getInstance() {
        CopilotEditorTestManager copilotEditorTestManager = (CopilotEditorTestManager)ApplicationManager.getApplication().getService(CopilotEditorManager.class);
        if (copilotEditorTestManager == null) {
            CopilotEditorTestManager.$$$reportNull$$$0(0);
        }
        return copilotEditorTestManager;
    }

        @RequiresEdt
    public List<CopilotInlayRenderer> collectInlays(Editor editor) {
        if (editor == null) {
            CopilotEditorTestManager.$$$reportNull$$$0(1);
        }
        Document document = editor.getDocument();
        List<CopilotInlayRenderer> list = this.collectInlays(editor, 0, document.getTextLength());
        if (list == null) {
            CopilotEditorTestManager.$$$reportNull$$$0(2);
        }
        return list;
    }

        @RequiresEdt
    public List<CopilotInlayRenderer> collectCurrentLineInlays(Editor editor) {
        if (editor == null) {
            CopilotEditorTestManager.$$$reportNull$$$0(3);
        }
        Document document = editor.getDocument();
        int line = document.getLineNumber(editor.getCaretModel().getOffset());
        int lineStart = document.getLineStartOffset(line);
        int lineEnd = document.getLineEndOffset(line);
        List<CopilotInlayRenderer> list = this.collectInlays(editor, lineStart, lineEnd);
        if (list == null) {
            CopilotEditorTestManager.$$$reportNull$$$0(4);
        }
        return list;
    }

    public void withDisabled(ThrowableRunnable<Exception> action) throws Exception {
        if (action == null) {
            CopilotEditorTestManager.$$$reportNull$$$0(5);
        }
        try {
            this.disabled = true;
            action.run();
        }
        finally {
            this.disabled = false;
        }
    }

    @Override
    public boolean isAvailable(Editor editor) {
        if (editor == null) {
            CopilotEditorTestManager.$$$reportNull$$$0(6);
        }
        return !this.disabled && super.isAvailable(editor);
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "method %s.%s must not return null";
                break;
            }
            case 1: 
            case 3: 
            case 5: 
            case 6: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1: 
            case 3: 
            case 5: 
            case 6: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/editor/CopilotEditorTestManager";
                break;
            }
            case 1: 
            case 3: 
            case 6: {
                objectArray2 = objectArray3;
                objectArray3[0] = "editor";
                break;
            }
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "action";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getInstance";
                break;
            }
            case 1: 
            case 3: 
            case 5: 
            case 6: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/editor/CopilotEditorTestManager";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "collectInlays";
                break;
            }
            case 4: {
                objectArray = objectArray2;
                objectArray2[1] = "collectCurrentLineInlays";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: {
                objectArray = objectArray;
                objectArray[2] = "collectInlays";
                break;
            }
            case 3: {
                objectArray = objectArray;
                objectArray[2] = "collectCurrentLineInlays";
                break;
            }
            case 5: {
                objectArray = objectArray;
                objectArray[2] = "withDisabled";
                break;
            }
            case 6: {
                objectArray = objectArray;
                objectArray[2] = "isAvailable";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
            case 1: 
            case 3: 
            case 5: 
            case 6: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

