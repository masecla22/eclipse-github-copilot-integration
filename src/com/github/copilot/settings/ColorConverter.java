/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.ui.ColorUtil
 *  com.intellij.util.xmlb.Converter
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.settings;

import com.intellij.ui.ColorUtil;
import com.intellij.util.xmlb.Converter;
import java.awt.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColorConverter
extends Converter<Color> {
        public Color fromString(String value) {
        if (value == null) {
            throw new IllegalStateException("value cannot be null!");
        }
        try {
            return ColorUtil.fromHex((String)value);
        }
        catch (Exception e) {
            return null;
        }
    }

        public String toString(Color value) {
        if (value == null) {
            throw new IllegalStateException("value cannot be null!");
        }
        return ColorUtil.toHtmlColor((Color)value);
    }

    
}

