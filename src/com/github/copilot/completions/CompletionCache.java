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

@ThreadSafe
public interface CompletionCache {
	public boolean isLatestPrefix(String var1);

	public List<CopilotCompletion> get(String var1, boolean var2);

	public List<CopilotCompletion> getLatest(String var1);

	public void add(String var1, String var2, boolean var3, CopilotCompletion var4);

	public void updateLatest(String var1, String var2, boolean var3);

	public void clear();
}
