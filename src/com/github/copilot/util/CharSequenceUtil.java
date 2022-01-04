/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.util;

public final class CharSequenceUtil {
	public static int lastIndexOf(CharSequence text, char ch) {
		if (text == null) {
			throw new IllegalStateException("text cannot be null!");
		}
		if (text instanceof String) {
			return ((String) text).lastIndexOf(ch);
		}
		for (int i = text.length() - 1; i >= 0; --i) {
			if (text.charAt(i) != ch)
				continue;
			return i;
		}
		return -1;
	}

}
