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
    @NotNull
    public LineInfo getLineInfo();

    public boolean equalsRequest(@NotNull EditorRequest var1);

    @NotNull
    default public String getCurrentDocumentPrefix() {
        String string = this.getDocumentContent().substring(0, this.getOffset());
        if (string == null) {
            EditorRequest.$$$reportNull$$$0(0);
        }
        return string;
    }

    @NotNull
    public String getDocumentContent();

    @NotNull
    public Language getFileLanguage();

    @NotNull
    public String getRelativeFilePath();

    @NotNull
    public Project getProject();

    @NotNull
    public CompletionType getCompletionType();

    public int getOffset();

    public boolean isUseTabIndents();

    public int getTabWidth();

    public int getRequestId();

    public Disposable getDisposable();

    public long getRequestTimestamp();

    @Nullable
    public Integer getDocumentModificationSequence();

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "com/github/copilot/request/EditorRequest", "getCurrentDocumentPrefix"));
    }
}

