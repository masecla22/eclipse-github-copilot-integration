/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.util.TextRange
 *  com.intellij.util.concurrency.annotations.RequiresEdt
 *  com.intellij.util.messages.Topic
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.editor;

import com.github.copilot.completions.CopilotInlayList;
import com.github.copilot.editor.CopilotInlayRenderer;
import com.github.copilot.editor.InlayDisposeContext;
import com.github.copilot.editor.InlayMessage;
import com.github.copilot.request.EditorRequest;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.concurrency.annotations.RequiresEdt;
import com.intellij.util.messages.Topic;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface CopilotEditorManager
extends Disposable {
    public static final Topic<InlayMessage> INLAY_TOPIC = Topic.create((String)"copilot.inlayUpdate", InlayMessage.class);

    @NotNull
    public static CopilotEditorManager getInstance() {
        CopilotEditorManager copilotEditorManager = (CopilotEditorManager)ApplicationManager.getApplication().getService(CopilotEditorManager.class);
        if (copilotEditorManager == null) {
            CopilotEditorManager.$$$reportNull$$$0(0);
        }
        return copilotEditorManager;
    }

    @RequiresEdt
    public boolean isAvailable(@NotNull Editor var1);

    @RequiresEdt
    default public boolean hasCompletionInlays(@NotNull Editor editor) {
        if (editor == null) {
            CopilotEditorManager.$$$reportNull$$$0(1);
        }
        return this.countCompletionInlays(editor, TextRange.from((int)0, (int)editor.getDocument().getTextLength()), true, true, true, true) > 0;
    }

    @RequiresEdt
    public int countCompletionInlays(@NotNull Editor var1, @NotNull TextRange var2, boolean var3, boolean var4, boolean var5, boolean var6);

    @RequiresEdt
    public boolean hasTypingAsSuggestedData(@NotNull Editor var1, char var2);

    @RequiresEdt
    public boolean applyCompletion(@NotNull Editor var1);

    @RequiresEdt
    public void applyCompletion(@NotNull Project var1, @NotNull Editor var2, @NotNull EditorRequest var3, @NotNull CopilotInlayList var4);

    @RequiresEdt
    public void disposeInlays(@NotNull Editor var1, @NotNull InlayDisposeContext var2);

    public void editorModified(@NotNull Editor var1);

    default public void editorModified(@NotNull Editor editor, boolean force) {
        if (editor == null) {
            CopilotEditorManager.$$$reportNull$$$0(2);
        }
        this.editorModified(editor, editor.getCaretModel().getOffset(), force);
    }

    @RequiresEdt
    default public void editorModified(@NotNull Editor editor, int offset) {
        if (editor == null) {
            CopilotEditorManager.$$$reportNull$$$0(3);
        }
        this.editorModified(editor, offset, false);
    }

    @RequiresEdt
    public void editorModified(@NotNull Editor var1, int var2, boolean var3);

    @RequiresEdt
    public void cancelCompletionRequests(@NotNull Editor var1);

    @NotNull
    @RequiresEdt
    public List<CopilotInlayRenderer> collectInlays(@NotNull Editor var1, int var2, int var3);

    public boolean hasNextInlaySet(@NotNull Editor var1);

    public boolean hasPreviousInlaySet(@NotNull Editor var1);

    public void showNextInlaySet(@NotNull Editor var1);

    public void showPreviousInlaySet(@NotNull Editor var1);

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "@NotNull method %s.%s must not return null";
                break;
            }
            case 1: 
            case 2: 
            case 3: {
                string = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1: 
            case 2: 
            case 3: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/editor/CopilotEditorManager";
                break;
            }
            case 1: 
            case 2: 
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "editor";
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
            case 2: 
            case 3: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/editor/CopilotEditorManager";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: {
                objectArray = objectArray;
                objectArray[2] = "hasCompletionInlays";
                break;
            }
            case 2: 
            case 3: {
                objectArray = objectArray;
                objectArray[2] = "editorModified";
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
            case 2: 
            case 3: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

