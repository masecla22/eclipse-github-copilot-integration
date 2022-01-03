/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang;

import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CommonLanguageSupport {
    private static final Pattern EOL_PATTERN = Pattern.compile("^\\s*[)}\\]\"'`]*\\s*[:{;,]?\\s*$");

    private CommonLanguageSupport() {
    }

    public static boolean isMiddleOfTheLine(String lineSuffix) {
        if (lineSuffix == null) {
            throw new IllegalStateException("lineSuffix cannot be null!");
        }
        return !lineSuffix.trim().isEmpty();
    }

    public static boolean isValidMiddleOfTheLinePosition(String lineSuffix) {
        if (lineSuffix == null) {
            throw new IllegalStateException("lineSuffix cannot be null!");
        }
        return EOL_PATTERN.matcher(lineSuffix.trim()).matches();
    }

        public static Boolean isInlineSuggestion(String lineSuffix) {
        if (lineSuffix == null) {
            throw new IllegalStateException("lineSuffix cannot be null!");
        }
        boolean isMiddleOfLine = CommonLanguageSupport.isMiddleOfTheLine(lineSuffix);
        boolean isValidMiddleOfLine = CommonLanguageSupport.isValidMiddleOfTheLinePosition(lineSuffix);
        if (isMiddleOfLine && !isValidMiddleOfLine) {
            return null;
        }
        return isMiddleOfLine;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        objectArray2[0] = "lineSuffix";
        objectArray2[1] = "com/github/copilot/lang/CommonLanguageSupport";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "isMiddleOfTheLine";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[2] = "isValidMiddleOfTheLinePosition";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[2] = "isInlineSuggestion";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

