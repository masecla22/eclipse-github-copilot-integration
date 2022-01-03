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

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        objectArray2[0] = "value";
        objectArray2[1] = "com/github/copilot/settings/ColorConverter";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "fromString";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[2] = "toString";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

