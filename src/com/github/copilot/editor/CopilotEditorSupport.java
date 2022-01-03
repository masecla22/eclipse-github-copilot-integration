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

    public static boolean isEditorCompletionsSupported(@NotNull Editor editor) {
        if (editor == null) {
            CopilotEditorSupport.$$$reportNull$$$0(0);
        }
        if (!EP.hasAnyExtensions()) {
            return true;
        }
        return EP.findFirstSafe(editorSupport -> !editorSupport.isCompletionsEnabled(editor)) == null;
    }

    public boolean isCompletionsEnabled(@NotNull Editor var1);

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "editor", "com/github/copilot/editor/CopilotEditorSupport", "isEditorCompletionsSupported"));
    }
}

