/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.project.Project
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.github;

import com.intellij.openapi.project.Project;

@FunctionalInterface
public interface UnauthorizedTokenCallback {
	public void onUnauthorizedToken(Project var1, String var2);
}
