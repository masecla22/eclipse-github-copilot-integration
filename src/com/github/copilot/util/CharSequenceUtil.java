/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.util;

import org.jetbrains.annotations.NotNull;

public final class CharSequenceUtil {
    public static int lastIndexOf(@NotNull CharSequence text, char ch) {
        if (text == null) {
            CharSequenceUtil.$$$reportNull$$$0(0);
        }
        if (text instanceof String) {
            return ((String)text).lastIndexOf(ch);
        }
        for (int i = text.length() - 1; i >= 0; --i) {
            if (text.charAt(i) != ch) continue;
            return i;
        }
        return -1;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "text", "com/github/copilot/util/CharSequenceUtil", "lastIndexOf"));
    }
}

