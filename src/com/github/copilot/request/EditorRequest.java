/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.lang.Language
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.project.Project
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.request;

import com.github.copilot.request.CompletionType;
import com.github.copilot.request.LineInfo;
import com.github.copilot.util.Cancellable;
import com.intellij.lang.Language;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EditorRequest
extends Cancellable {
        public LineInfo getLineInfo();

    public boolean equalsRequest(EditorRequest var1);

        default public String getCurrentDocumentPrefix() {
        String string = this.getDocumentContent().substring(0, this.getOffset());
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

        public String getDocumentContent();

        public Language getFileLanguage();

        public String getRelativeFilePath();

        public Project getProject();

        public CompletionType getCompletionType();

    public int getOffset();

    public boolean isUseTabIndents();

    public int getTabWidth();

    public int getRequestId();

    public Disposable getDisposable();

    public long getRequestTimestamp();

        public Integer getDocumentModificationSequence();

    
}

