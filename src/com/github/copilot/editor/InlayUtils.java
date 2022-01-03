/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.editor.InlayProperties
 */
package com.github.copilot.editor;

import com.intellij.openapi.editor.InlayProperties;

class InlayUtils {
    InlayUtils() {
    }

    static InlayProperties createInlineProperties(int n) {
        return new InlayProperties().relatesToPrecedingText(true).showAbove(false).priority(Integer.MAX_VALUE - n);
    }

    static InlayProperties createAfterLineEndProperties(int n) {
        return new InlayProperties().relatesToPrecedingText(true).showAbove(false).priority(Integer.MAX_VALUE - n);
    }

    static InlayProperties createBlockProperties(int n) {
        return new InlayProperties().relatesToPrecedingText(true).showAbove(false).priority(Integer.MAX_VALUE - n);
    }
}

