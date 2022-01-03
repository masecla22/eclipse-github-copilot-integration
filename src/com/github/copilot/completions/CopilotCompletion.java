/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.completions;

import java.util.List;

public interface CopilotCompletion {
        public List<String> getCompletion();

        public CopilotCompletion asCached();

        public CopilotCompletion withoutPrefix(String var1);

        public CopilotCompletion withCompletion(List<String> var1);
}

