/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.concurrent.Immutable
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.editor;

import com.github.copilot.request.EditorRequest;
import javax.annotation.concurrent.Immutable;

@Immutable
public interface InlayMessage {
    public void inlaysUpdated(EditorRequest var1);
}

