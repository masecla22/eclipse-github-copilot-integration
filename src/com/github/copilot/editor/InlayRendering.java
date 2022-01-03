/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.ide.ui.AntialiasingType
 *  com.intellij.ide.ui.UISettings
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.editor.colors.EditorFontType
 *  com.intellij.openapi.editor.impl.FontInfo
 *  com.intellij.openapi.util.Key
 *  com.intellij.openapi.util.UserDataHolder
 *  com.intellij.util.ui.UIUtil
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.editor;

import com.github.copilot.util.Maps;
import com.intellij.ide.ui.AntialiasingType;
import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.editor.impl.FontInfo;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.util.ui.UIUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

final class InlayRendering {
    private static final Key<Map<Font, FontMetrics>> KEY_CACHED_FONTMETRICS = Key.create((String)"copilot.editorFontMetrics");

    private InlayRendering() {
    }

    static int calculateWidth(Editor editor, String text, List<String> textLines) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (text == null) {
            throw new IllegalStateException("text cannot be null!");
        }
        if (textLines == null) {
            throw new IllegalStateException("textLines cannot be null!");
        }
        FontMetrics metrics = InlayRendering.fontMetrics(editor, InlayRendering.getFont(editor, text));
        int maxWidth = 0;
        for (String line : textLines) {
            maxWidth = Math.max(maxWidth, metrics.stringWidth(line));
        }
        return maxWidth;
    }

    static void renderCodeBlock(Editor editor, String content, List<String> contentLines, Graphics2D g, Rectangle2D region, Color textColor) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (content == null) {
            throw new IllegalStateException("content cannot be null!");
        }
        if (contentLines == null) {
            throw new IllegalStateException("contentLines cannot be null!");
        }
        if (g == null) {
            throw new IllegalStateException("g cannot be null!");
        }
        if (region == null) {
            throw new IllegalStateException("region cannot be null!");
        }
        if (textColor == null) {
            throw new IllegalStateException("textColor cannot be null!");
        }
        if (content.isEmpty() || contentLines.isEmpty()) {
            return;
        }
        Graphics2D g2 = (Graphics2D)g.create();
        UISettings.setupAntialiasing((Graphics)g2);
        g2.setColor(textColor);
        Font font = InlayRendering.getFont(editor, content);
        g2.setFont(font);
        FontMetrics metrics = InlayRendering.fontMetrics(editor, font);
        int lineHeight = editor.getLineHeight();
        double fontBaseline = Math.ceil(font.createGlyphVector(metrics.getFontRenderContext(), "Alb").getVisualBounds().getHeight());
        float linePadding = (float)((double)lineHeight - fontBaseline) / 2.0f;
        g2.translate(region.getX(), region.getY());
        g2.setClip(0, 0, (int)region.getWidth(), (int)region.getHeight());
        for (String line : contentLines) {
            g2.drawString(line, 0.0f, (float)fontBaseline + linePadding);
            g2.translate(0, lineHeight);
        }
        g2.dispose();
    }

    private static FontMetrics fontMetrics(Editor editor, Font font) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (font == null) {
            throw new IllegalStateException("font cannot be null!");
        }
        FontRenderContext editorContext = FontInfo.getFontRenderContext((Component)editor.getContentComponent());
        FontRenderContext context = new FontRenderContext(editorContext.getTransform(), AntialiasingType.getKeyForCurrentScope((boolean)false), UISettings.getEditorFractionalMetricsHint());
        Map cachedMap = (Map)KEY_CACHED_FONTMETRICS.get((UserDataHolder)editor, Collections.emptyMap());
        FontMetrics fontMetrics = (FontMetrics)cachedMap.get(font);
        if (fontMetrics == null || !context.equals(fontMetrics.getFontRenderContext())) {
            fontMetrics = FontInfo.getFontMetrics((Font)font, (FontRenderContext)context);
            KEY_CACHED_FONTMETRICS.set((UserDataHolder)editor, Maps.merge(cachedMap, Map.of(font, fontMetrics)));
        }
        return fontMetrics;
    }

        private static Font getFont(Editor editor, String text) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (text == null) {
            throw new IllegalStateException("text cannot be null!");
        }
        Font font = editor.getColorsScheme().getFont(EditorFontType.PLAIN).deriveFont(2);
        Font fallbackFont = UIUtil.getFontWithFallbackIfNeeded((Font)font, (String)text);
        Font font2 = fallbackFont.deriveFont((float)InlayRendering.fontSize(editor));
        if (font2 == null) {
            InlayRendering.$$$reportNull$$$0(13);
        }
        return font2;
    }

    private static int fontSize(Editor editor) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        return editor.getColorsScheme().getEditorFontSize();
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
            case 13: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 13: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "editor";
                break;
            }
            case 1: 
            case 12: {
                objectArray2 = objectArray3;
                objectArray3[0] = "text";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "textLines";
                break;
            }
            case 4: {
                objectArray2 = objectArray3;
                objectArray3[0] = "content";
                break;
            }
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "contentLines";
                break;
            }
            case 6: {
                objectArray2 = objectArray3;
                objectArray3[0] = "g";
                break;
            }
            case 7: {
                objectArray2 = objectArray3;
                objectArray3[0] = "region";
                break;
            }
            case 8: {
                objectArray2 = objectArray3;
                objectArray3[0] = "textColor";
                break;
            }
            case 10: {
                objectArray2 = objectArray3;
                objectArray3[0] = "font";
                break;
            }
            case 13: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/editor/InlayRendering";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/editor/InlayRendering";
                break;
            }
            case 13: {
                objectArray = objectArray2;
                objectArray2[1] = "getFont";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "calculateWidth";
                break;
            }
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: {
                objectArray = objectArray;
                objectArray[2] = "renderCodeBlock";
                break;
            }
            case 9: 
            case 10: {
                objectArray = objectArray;
                objectArray[2] = "fontMetrics";
                break;
            }
            case 11: 
            case 12: {
                objectArray = objectArray;
                objectArray[2] = "getFont";
                break;
            }
            case 13: {
                break;
            }
            case 14: {
                objectArray = objectArray;
                objectArray[2] = "fontSize";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 13: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

