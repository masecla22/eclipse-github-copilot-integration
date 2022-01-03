/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.util;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ObjectToIntFunction<T> {
    public int apply(@NotNull T var1);
}

