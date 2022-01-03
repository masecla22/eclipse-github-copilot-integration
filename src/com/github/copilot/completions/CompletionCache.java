/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.concurrent.ThreadSafe
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.completions;

import com.github.copilot.completions.CopilotCompletion;
import java.util.List;
import javax.annotation.concurrent.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ThreadSafe
public interface CompletionCache {
    public boolean isLatestPrefix(@NotNull String var1);

    @Nullable
    public List<CopilotCompletion> get(@NotNull String var1, boolean var2);

    @Nullable
    public List<CopilotCompletion> getLatest(@NotNull String var1);

    public void add(@NotNull String var1, @NotNull String var2, boolean var3, @NotNull CopilotCompletion var4);

    public void updateLatest(@NotNull String var1, @NotNull String var2, boolean var3);

    public void clear();
}

