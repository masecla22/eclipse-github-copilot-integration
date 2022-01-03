/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.codeInsight.actions.ReaderModeMatcher
 *  com.intellij.codeInsight.actions.ReaderModeProvider$ReaderMode
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.vfs.VirtualFile
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.toolWindow;

import com.github.copilot.toolWindow.OpenCopilotHandler;
import com.intellij.codeInsight.actions.ReaderModeMatcher;
import com.intellij.codeInsight.actions.ReaderModeProvider;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class OpenCopilotDisableReaderModeMatcher
implements ReaderModeMatcher {
        public Boolean matches(Project project, VirtualFile virtualFile, Editor editor, ReaderModeProvider.ReaderMode readerMode) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if (virtualFile == null) {
            throw new IllegalStateException("virtualFile cannot be null!");
        }
        if (readerMode == null) {
            throw new IllegalStateException("readerMode cannot be null!");
        }
        if (OpenCopilotHandler.isCopilotSnippetFile(virtualFile)) {
            return false;
        }
        return null;
    }

    
}

