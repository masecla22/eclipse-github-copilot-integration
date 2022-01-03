/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.util.text.StringUtil
 *  com.intellij.openapi.util.text.Strings
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.request;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.util.text.Strings;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public final class EditorRequestUtil {
    private EditorRequestUtil() {
    }

    public static List<String> fixIndentation(List<String> lines, boolean useTabIndents, int tabWidth) {
        if (lines == null) {
            EditorRequestUtil.$$$reportNull$$$0(0);
        }
        if (useTabIndents) {
            return lines.stream().map(line -> {
                int tabs = Strings.countChars((CharSequence)line, (char)' ', (int)0, (boolean)true) / tabWidth;
                int spaces = tabs * tabWidth;
                return StringUtil.repeatSymbol((char)'\t', (int)tabs) + line.substring(spaces);
            }).collect(Collectors.toList());
        }
        return lines.stream().map(line -> {
            int tabs = Strings.countChars((CharSequence)line, (char)'\t', (int)0, (boolean)true);
            int spaces = tabs * tabWidth;
            return StringUtil.repeatSymbol((char)' ', (int)spaces) + line.substring(tabs);
        }).collect(Collectors.toList());
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", "lines", "com/github/copilot/request/EditorRequestUtil", "fixIndentation"));
    }
}

