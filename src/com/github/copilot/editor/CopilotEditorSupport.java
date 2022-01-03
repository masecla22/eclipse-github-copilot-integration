/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.extensions.ExtensionPointName
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.editor;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.extensions.ExtensionPointName;
import org.jetbrains.annotations.NotNull;

public interface CopilotEditorSupport {
    public static final ExtensionPointName<CopilotEditorSupport> EP = ExtensionPointName.create((String)"com.github.copilot.editorSupport");

    public static boolean isEditorCompletionsSupported(Editor editor) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (!EP.hasAnyExtensions()) {
            return true;
        }
        return EP.findFirstSafe(editorSupport -> !editorSupport.isCompletionsEnabled(editor)) == null;
    }

    public boolean isCompletionsEnabled(Editor var1);

    
}

