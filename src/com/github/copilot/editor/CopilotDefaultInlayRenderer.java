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

class CopilotDefaultInlayRenderer
implements CopilotInlayRenderer {
        private final List<String> lines;
        private final String content;
        private final CopilotCompletionType type;
        private Inlay<CopilotInlayRenderer> inlay;
    private int cachedWidth;
    private int cachedHeight;

    CopilotDefaultInlayRenderer(EditorRequest request, CopilotCompletionType type, List<String> lines) {
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
        }
        if (type == null) {
            throw new IllegalStateException("type cannot be null!");
        }
        if (lines == null) {
            throw new IllegalStateException("lines cannot be null!");
        }
        this.cachedWidth = -1;
        this.cachedHeight = -1;
        this.lines = CopilotDefaultInlayRenderer.replaceLeadingTabs(lines, request);
        this.type = type;
        this.content = StringUtils.join(lines, (String)"\n");
    }

    @Override
        public Inlay<CopilotInlayRenderer> getInlay() {
        assert (this.inlay != null);
        Inlay<CopilotInlayRenderer> inlay = this.inlay;
        if (inlay == null) {
            throw new IllegalStateException("inlay cannot be null!");
        }
        return inlay;
    }

    public void setInlay(Inlay<CopilotInlayRenderer> inlay) {
        if (inlay == null) {
            throw new IllegalStateException("inlay cannot be null!");
        }
        this.inlay = inlay;
    }

    @Override
        public CopilotCompletionType getType() {
        CopilotCompletionType copilotCompletionType = this.type;
        if (copilotCompletionType == null) {
            throw new IllegalStateException("copilotCompletionType cannot be null!");
        }
        return copilotCompletionType;
    }

    @Override
        public List<String> getContentLines() {
        List<String> list = this.lines;
        if (list == null) {
            throw new IllegalStateException("list cannot be null!");
        }
        return list;
    }

    public int calcHeightInPixels(Inlay inlay) {
        if (inlay == null) {
            throw new IllegalStateException("inlay cannot be null!");
        }
        if (this.cachedHeight < 0) {
            this.cachedHeight = inlay.getEditor().getLineHeight() * this.lines.size();
            return this.cachedHeight;
        }
        return this.cachedHeight;
    }

    public int calcWidthInPixels(Inlay inlay) {
        if (inlay == null) {
            throw new IllegalStateException("inlay cannot be null!");
        }
        if (this.cachedWidth < 0) {
            int width = InlayRendering.calculateWidth(inlay.getEditor(), this.content, this.lines);
            this.cachedWidth = Math.max(1, width);
            return this.cachedWidth;
        }
        return this.cachedWidth;
    }

    public void paint(Inlay inlay, Graphics2D g, Rectangle2D region, TextAttributes textAttributes) {
        Editor editor;
        if (inlay == null) {
            throw new IllegalStateException("inlay cannot be null!");
        }
        if (g == null) {
            throw new IllegalStateException("g cannot be null!");
        }
        if (region == null) {
            throw new IllegalStateException("region cannot be null!");
        }
        if (textAttributes == null) {
            throw new IllegalStateException("textAttributes cannot be null!");
        }
        if ((editor = inlay.getEditor()).isDisposed()) {
            return;
        }
        InlayRendering.renderCodeBlock(editor, this.content, this.lines, g, region, CopilotDefaultInlayRenderer.getTextColor(editor, textAttributes));
    }

    static List<String> replaceLeadingTabs(List<String> lines, EditorRequest request) {
        if (lines == null) {
            throw new IllegalStateException("lines cannot be null!");
        }
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
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

    private static Color getTextColor(Editor editor, TextAttributes contextAttributes) {
        Color userColor;
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (contextAttributes == null) {
            throw new IllegalStateException("contextAttributes cannot be null!");
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

        public List<String> getLines() {
        List<String> list = this.lines;
        if (list == null) {
            throw new IllegalStateException("list cannot be null!");
        }
        return list;
    }

        public String getContent() {
        String string = this.content;
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
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

    
}

