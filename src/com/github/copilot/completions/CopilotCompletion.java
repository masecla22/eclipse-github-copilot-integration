/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.completions;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CopilotCompletion {
    @NotNull
    public List<String> getCompletion();

    @NotNull
    public CopilotCompletion asCached();

    @Nullable
    public CopilotCompletion withoutPrefix(@NotNull String var1);

    @NotNull
    public CopilotCompletion withCompletion(@NotNull List<String> var1);
}

