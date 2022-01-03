/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.editor.DefaultLanguageHighlighterColors
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.editor.Inlay
 *  com.intellij.openapi.editor.markup.TextAttributes
 *  com.intellij.openapi.util.text.StringUtil
 *  com.intellij.openapi.util.text.Strings
 *  org.apache.commons.lang.StringUtils
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.editor;

import com.github.copilot.completions.CopilotCompletionType;
import com.github.copilot.editor.CopilotInlayRenderer;
import com.github.copilot.editor.InlayRendering;
import com.github.copilot.request.EditorRequest;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.util.text.Strings;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class CopilotDefaultInlayRenderer
implements CopilotInlayRenderer {
    @NotNull
    private final List<String> lines;
    @NotNull
    private final String content;
    @NotNull
    private final CopilotCompletionType type;
    @Nullable
    private Inlay<CopilotInlayRenderer> inlay;
    private int cachedWidth;
    private int cachedHeight;

    CopilotDefaultInlayRenderer(@NotNull EditorRequest request, @NotNull CopilotCompletionType type, @NotNull List<String> lines) {
        if (request == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(0);
        }
        if (type == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(1);
        }
        if (lines == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(2);
        }
        this.cachedWidth = -1;
        this.cachedHeight = -1;
        this.lines = CopilotDefaultInlayRenderer.replaceLeadingTabs(lines, request);
        this.type = type;
        this.content = StringUtils.join(lines, (String)"\n");
    }

    @Override
    @NotNull
    public Inlay<CopilotInlayRenderer> getInlay() {
        assert (this.inlay != null);
        Inlay<CopilotInlayRenderer> inlay = this.inlay;
        if (inlay == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(3);
        }
        return inlay;
    }

    public void setInlay(@NotNull Inlay<CopilotInlayRenderer> inlay) {
        if (inlay == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(4);
        }
        this.inlay = inlay;
    }

    @Override
    @NotNull
    public CopilotCompletionType getType() {
        CopilotCompletionType copilotCompletionType = this.type;
        if (copilotCompletionType == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(5);
        }
        return copilotCompletionType;
    }

    @Override
    @NotNull
    public List<String> getContentLines() {
        List<String> list = this.lines;
        if (list == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(6);
        }
        return list;
    }

    public int calcHeightInPixels(@NotNull Inlay inlay) {
        if (inlay == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(7);
        }
        if (this.cachedHeight < 0) {
            this.cachedHeight = inlay.getEditor().getLineHeight() * this.lines.size();
            return this.cachedHeight;
        }
        return this.cachedHeight;
    }

    public int calcWidthInPixels(@NotNull Inlay inlay) {
        if (inlay == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(8);
        }
        if (this.cachedWidth < 0) {
            int width = InlayRendering.calculateWidth(inlay.getEditor(), this.content, this.lines);
            this.cachedWidth = Math.max(1, width);
            return this.cachedWidth;
        }
        return this.cachedWidth;
    }

    public void paint(@NotNull Inlay inlay, @NotNull Graphics2D g, @NotNull Rectangle2D region, @NotNull TextAttributes textAttributes) {
        Editor editor;
        if (inlay == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(9);
        }
        if (g == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(10);
        }
        if (region == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(11);
        }
        if (textAttributes == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(12);
        }
        if ((editor = inlay.getEditor()).isDisposed()) {
            return;
        }
        InlayRendering.renderCodeBlock(editor, this.content, this.lines, g, region, CopilotDefaultInlayRenderer.getTextColor(editor, textAttributes));
    }

    static List<String> replaceLeadingTabs(@NotNull List<String> lines, @NotNull EditorRequest request) {
        if (lines == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(13);
        }
        if (request == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(14);
        }
        return lines.stream().map(line -> {
            int tabCount = Strings.countChars((CharSequence)line, (char)'\t', (int)0, (boolean)true);
            if (tabCount > 0) {
                String tabSpaces = StringUtil.repeatSymbol((char)' ', (int)(tabCount * request.getTabWidth()));
                return tabSpaces + line.substring(tabCount);
            }
            return line;
        }).collect(Collectors.toList());
    }

    private static Color getTextColor(@NotNull Editor editor, @NotNull TextAttributes contextAttributes) {
        Color userColor;
        if (editor == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(15);
        }
        if (contextAttributes == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(16);
        }
        if ((userColor = CopilotApplicationSettings.settings().inlayTextColor) != null) {
            return userColor;
        }
        TextAttributes attributes = editor.getColorsScheme().getAttributes(DefaultLanguageHighlighterColors.INLAY_TEXT_WITHOUT_BACKGROUND);
        if (attributes == null) {
            attributes = contextAttributes;
        }
        return attributes.getForegroundColor();
    }

    @NotNull
    public List<String> getLines() {
        List<String> list = this.lines;
        if (list == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(17);
        }
        return list;
    }

    @NotNull
    public String getContent() {
        String string = this.content;
        if (string == null) {
            CopilotDefaultInlayRenderer.$$$reportNull$$$0(18);
        }
        return string;
    }

    public int getCachedWidth() {
        return this.cachedWidth;
    }

    public int getCachedHeight() {
        return this.cachedHeight;
    }

    public void setCachedWidth(int cachedWidth) {
        this.cachedWidth = cachedWidth;
    }

    public void setCachedHeight(int cachedHeight) {
        this.cachedHeight = cachedHeight;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CopilotDefaultInlayRenderer)) {
            return false;
        }
        CopilotDefaultInlayRenderer other = (CopilotDefaultInlayRenderer)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getCachedWidth() != other.getCachedWidth()) {
            return false;
        }
        if (this.getCachedHeight() != other.getCachedHeight()) {
            return false;
        }
        List<String> this$lines = this.getLines();
        List<String> other$lines = other.getLines();
        if (this$lines == null ? other$lines != null : !((Object)this$lines).equals(other$lines)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
            return false;
        }
        CopilotCompletionType this$type = this.getType();
        CopilotCompletionType other$type = other.getType();
        if (this$type == null ? other$type != null : !((Object)((Object)this$type)).equals((Object)other$type)) {
            return false;
        }
        Inlay<CopilotInlayRenderer> this$inlay = this.getInlay();
        Inlay<CopilotInlayRenderer> other$inlay = other.getInlay();
        return !(this$inlay == null ? other$inlay != null : !this$inlay.equals(other$inlay));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CopilotDefaultInlayRenderer;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getCachedWidth();
        result = result * 59 + this.getCachedHeight();
        List<String> $lines = this.getLines();
        result = result * 59 + ($lines == null ? 43 : ((Object)$lines).hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        CopilotCompletionType $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)((Object)$type)).hashCode());
        Inlay<CopilotInlayRenderer> $inlay = this.getInlay();
        result = result * 59 + ($inlay == null ? 43 : $inlay.hashCode());
        return result;
    }

    public String toString() {
        return "CopilotDefaultInlayRenderer(lines=" + this.getLines() + ", content=" + this.getContent() + ", type=" + this.getType() + ", cachedWidth=" + this.getCachedWidth() + ", cachedHeight=" + this.getCachedHeight() + ")";
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
            case 3: 
            case 5: 
            case 6: 
            case 17: 
            case 18: {
                string = "@NotNull method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 3: 
            case 5: 
            case 6: 
            case 17: 
            case 18: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "request";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "type";
                break;
            }
            case 2: 
            case 13: {
                objectArray2 = objectArray3;
                objectArray3[0] = "lines";
                break;
            }
            case 3: 
            case 5: 
            case 6: 
            case 17: 
            case 18: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/editor/CopilotDefaultInlayRenderer";
                break;
            }
            case 4: 
            case 7: 
            case 8: 
            case 9: {
                objectArray2 = objectArray3;
                objectArray3[0] = "inlay";
                break;
            }
            case 10: {
                objectArray2 = objectArray3;
                objectArray3[0] = "g";
                break;
            }
            case 11: {
                objectArray2 = objectArray3;
                objectArray3[0] = "region";
                break;
            }
            case 12: {
                objectArray2 = objectArray3;
                objectArray3[0] = "textAttributes";
                break;
            }
            case 15: {
                objectArray2 = objectArray3;
                objectArray3[0] = "editor";
                break;
            }
            case 16: {
                objectArray2 = objectArray3;
                objectArray3[0] = "contextAttributes";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/editor/CopilotDefaultInlayRenderer";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[1] = "getInlay";
                break;
            }
            case 5: {
                objectArray = objectArray2;
                objectArray2[1] = "getType";
                break;
            }
            case 6: {
                objectArray = objectArray2;
                objectArray2[1] = "getContentLines";
                break;
            }
            case 17: {
                objectArray = objectArray2;
                objectArray2[1] = "getLines";
                break;
            }
            case 18: {
                objectArray = objectArray2;
                objectArray2[1] = "getContent";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "<init>";
                break;
            }
            case 3: 
            case 5: 
            case 6: 
            case 17: 
            case 18: {
                break;
            }
            case 4: {
                objectArray = objectArray;
                objectArray[2] = "setInlay";
                break;
            }
            case 7: {
                objectArray = objectArray;
                objectArray[2] = "calcHeightInPixels";
                break;
            }
            case 8: {
                objectArray = objectArray;
                objectArray[2] = "calcWidthInPixels";
                break;
            }
            case 9: 
            case 10: 
            case 11: 
            case 12: {
                objectArray = objectArray;
                objectArray[2] = "paint";
                break;
            }
            case 13: 
            case 14: {
                objectArray = objectArray;
                objectArray[2] = "replaceLeadingTabs";
                break;
            }
            case 15: 
            case 16: {
                objectArray = objectArray;
                objectArray[2] = "getTextColor";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 3: 
            case 5: 
            case 6: 
            case 17: 
            case 18: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

