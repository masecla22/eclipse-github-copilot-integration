/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.wm.ToolWindow
 *  com.intellij.util.ui.StatusText
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.toolWindow;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.util.ui.StatusText;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ToolWindowUtil {
    ToolWindowUtil() {
    }

    @Nullable
    static StatusText getEmptyText(@NotNull ToolWindow toolWindow) {
        Method method;
        if (toolWindow == null) {
            ToolWindowUtil.$$$reportNull$$$0(0);
        }
        if ((method = ToolWindowUtil.getEmptyText(toolWindow.getClass())) == null) {
            return null;
        }
        try {
            return (StatusText)method.invoke(toolWindow, new Object[0]);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    @Nullable
    static Method getEmptyText(Class<? extends ToolWindow> klazz) {
        try {
            return klazz.getMethod("getEmptyText", new Class[0]);
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "toolWindow", "com/github/copilot/toolWindow/ToolWindowUtil", "getEmptyText"));
    }
}

