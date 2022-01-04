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

final class ToolWindowUtil {
	ToolWindowUtil() {
	}

	static StatusText getEmptyText(ToolWindow toolWindow) {
		Method method;
		if (toolWindow == null) {
			throw new IllegalStateException("toolWindow cannot be null!");
		}
		if ((method = ToolWindowUtil.getEmptyText(toolWindow.getClass())) == null) {
			return null;
		}
		try {
			return (StatusText) method.invoke(toolWindow, new Object[0]);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return null;
		}
	}

	static Method getEmptyText(Class<? extends ToolWindow> klazz) {
		try {
			return klazz.getMethod("getEmptyText", new Class[0]);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

}
