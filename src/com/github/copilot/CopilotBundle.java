/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.DynamicBundle
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.PropertyKey
 */
package com.github.copilot;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public final class CopilotBundle
extends DynamicBundle {
    public static final CopilotBundle INSTANCE = new CopilotBundle();

    private CopilotBundle() {
        super("copilot.copilot");
    }

    public static String get(@NotNull @PropertyKey(resourceBundle="copilot.copilot") @NotNull @PropertyKey(resourceBundle="copilot.copilot") String key) {
        if (key == null) {
            CopilotBundle.$$$reportNull$$$0(0);
        }
        return INSTANCE.getMessage(key, new Object[0]);
    }

    public static String get(@NotNull @PropertyKey(resourceBundle="copilot.copilot") @NotNull @PropertyKey(resourceBundle="copilot.copilot") String key, Object ... params) {
        if (key == null) {
            CopilotBundle.$$$reportNull$$$0(1);
        }
        return INSTANCE.getMessage(key, params);
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "key", "com/github/copilot/CopilotBundle", "get"));
    }
}

