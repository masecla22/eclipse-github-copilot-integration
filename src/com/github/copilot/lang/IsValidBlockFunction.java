/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.psi.PsiElement
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface IsValidBlockFunction {
    public boolean isValidBlock(@NotNull PsiElement var1, boolean var2);
}

