/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.util.Pair
 *  com.intellij.openapi.util.ProperTextRange
 *  com.intellij.openapi.util.TextRange
 *  com.intellij.util.diff.Diff
 *  com.intellij.util.diff.Diff$Change
 *  com.intellij.util.diff.FilesTooBigForDiffException
 *  com.intellij.util.text.TextRanges
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.util;

import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.ProperTextRange;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.diff.Diff;
import com.intellij.util.diff.FilesTooBigForDiffException;
import com.intellij.util.text.TextRanges;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class CopilotStringUtil {
    private CopilotStringUtil() {
    }

        public static String trailingWhitespace(String text) {
        char ch;
        int endOffset;
        if (text == null) {
            throw new IllegalStateException("text cannot be null!");
        }
        if (text.isEmpty()) {
            return "";
        }
        for (endOffset = text.length(); endOffset > 0 && (ch = text.charAt(endOffset - 1)) != '\n' && Character.isWhitespace(ch); --endOffset) {
        }
        String string = text.substring(endOffset);
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

    public static int trailingWhitespaceLength(String text) {
        int length;
        char ch;
        int endOffset;
        if (text == null) {
            throw new IllegalStateException("text cannot be null!");
        }
        if (text.isEmpty()) {
            return 0;
        }
        for (endOffset = length = text.length(); endOffset > 0 && ((ch = text.charAt(endOffset - 1)) == ' ' || ch == '\t'); --endOffset) {
        }
        return length - endOffset;
    }

        public static String leadingWhitespace(String text) {
        if (text == null) {
            throw new IllegalStateException("text cannot be null!");
        }
        if (text.isEmpty()) {
            return "";
        }
        String string = text.substring(0, CopilotStringUtil.leadingWhitespaceLength(text));
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

    public static int leadingWhitespaceLength(String text) {
        char ch;
        int offset;
        if (text == null) {
            throw new IllegalStateException("text cannot be null!");
        }
        int length = text.length();
        for (offset = 0; offset < length && (ch = text.charAt(offset)) != '\n' && Character.isWhitespace(ch); ++offset) {
        }
        return offset;
    }

        public static String stripLeading(String text) {
        if (text == null) {
            throw new IllegalStateException("text cannot be null!");
        }
        if (text.isEmpty()) {
            return "";
        }
        int length = CopilotStringUtil.leadingWhitespaceLength(text);
        String string = length == 0 ? text : text.substring(length);
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

    public static int findOverlapLength(String withTrailing, String withLeading) {
        if (withTrailing == null) {
            throw new IllegalStateException("withTrailing cannot be null!");
        }
        if (withLeading == null) {
            throw new IllegalStateException("withLeading cannot be null!");
        }
        if (withTrailing.isEmpty() || withLeading.isEmpty()) {
            return 0;
        }
        int trailingLength = withTrailing.length();
        for (int i = 0; i <= trailingLength; ++i) {
            if (!withLeading.startsWith(withTrailing.substring(i))) continue;
            return trailingLength - i;
        }
        return 0;
    }

    public static int findOverlappingLines(List<String> withTrailing, List<String> withLeading) {
        if (withTrailing == null) {
            throw new IllegalStateException("withTrailing cannot be null!");
        }
        if (withLeading == null) {
            throw new IllegalStateException("withLeading cannot be null!");
        }
        if (withTrailing.isEmpty() || withLeading.isEmpty()) {
            return 0;
        }
        int trailingSize = withTrailing.size();
        int leadingSize = withLeading.size();
        int maxLines = Math.min(trailingSize, leadingSize);
        int overlapping = 0;
        for (int i = 1; i <= maxLines; ++i) {
            List<String> lines = withTrailing.subList(trailingSize - i, trailingSize);
            if (CopilotStringUtil.linesMatch(withLeading.subList(0, i), lines, true)) {
                overlapping = i;
                continue;
            }
            if (overlapping > 0) break;
        }
        return overlapping;
    }

        public static List<Pair<Integer, String>> createDiffInlays(String editor, String completion) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (completion == null) {
            throw new IllegalStateException("completion cannot be null!");
        }
        String commonPrefix = CopilotStringUtil.findCommonPrefix(completion, editor);
        String editorAdjusted = editor.substring(commonPrefix.length());
        String completionAdjusted = completion.substring(commonPrefix.length());
        int[] editorChars = editorAdjusted.chars().toArray();
        int[] completionChars = completionAdjusted.chars().toArray();
        CopilotStringUtil.patchCharPairs(completionChars);
        int patchDelta = commonPrefix.length();
        try {
            Diff.Change changelist = Diff.buildChanges((int[])editorChars, (int[])completionChars);
            if (changelist == null) {
                return null;
            }
            LinkedList<Pair<Integer, String>> result = new LinkedList<Pair<Integer, String>>();
            ArrayList changes = changelist.toList();
            for (Diff.Change change : changes) {
                if (change.inserted <= 0) continue;
                result.add((Pair<Integer, String>)Pair.create((Object)(change.line0 + patchDelta), (Object)CopilotStringUtil.unpatchCharPairs(completionChars, change.line1, change.inserted)));
            }
            return result;
        }
        catch (FilesTooBigForDiffException e) {
            return null;
        }
    }

    private static String findCommonPrefix(String data, String reference) {
        if (data == null) {
            throw new IllegalStateException("data cannot be null!");
        }
        if (reference == null) {
            throw new IllegalStateException("reference cannot be null!");
        }
        int maxSize = Math.min(data.length(), reference.length());
        int first = 0;
        for (int i = 0; i < maxSize && data.charAt(i) == reference.charAt(i); ++i) {
            ++first;
        }
        return data.substring(0, first);
    }

    public static List<String> getNextLines(String text, int offset, int maxLines) {
        if (text == null) {
            throw new IllegalStateException("text cannot be null!");
        }
        LinkedList<String> lines = new LinkedList<String>();
        int last = offset;
        for (int done = 0; done < maxLines; ++done) {
            int next = text.indexOf(10, last);
            if (next == -1) {
                if (text.length() <= last) break;
                lines.add(text.substring(last));
                break;
            }
            lines.add(text.substring(last, next));
            last = next + 1;
        }
        return lines;
    }

    public static boolean isSpaceOrTab(char c, boolean withNewline) {
        return c == ' ' || c == '\t' || withNewline && c == '\n';
    }

    public static boolean isSpacesOrTabs(CharSequence text, boolean withNewlines) {
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (CopilotStringUtil.isSpaceOrTab(c, withNewlines)) continue;
            return false;
        }
        return true;
    }

    public static boolean linesMatch(Iterable<String> a, Iterable<String> b, boolean trimEnd) {
        if (a == null) {
            throw new IllegalStateException("a cannot be null!");
        }
        if (b == null) {
            throw new IllegalStateException("b cannot be null!");
        }
        Iterator<String> itA = a.iterator();
        Iterator<String> itB = b.iterator();
        while (itA.hasNext() && itB.hasNext()) {
            String itemA = itA.next();
            String itemB = itB.next();
            boolean match = trimEnd ? itemA.stripTrailing().equals(itemB.stripTrailing()) : itemA.equals(itemB);
            if (match) continue;
            return false;
        }
        return !itA.hasNext() && !itB.hasNext();
    }

    static int[] patchCharPairs(int[] chars) {
        int parenChar = 65536;
        TextRanges stringRanges = CopilotStringUtil.findStringRanges(chars);
        for (int i = 0; i < chars.length; ++i) {
            int closeIndex;
            int c = chars[i];
            if ((c == 40 || c == 41) && CopilotStringUtil.isInRange(stringRanges, i)) {
                chars[i] = 65536 + (c == 41 ? 1 : 0);
                continue;
            }
            if (c != 40 || (closeIndex = CopilotStringUtil.firstMatchingPair(chars, i + 1, ')', '(', stringRanges)) == -1) continue;
            chars[i] = 65536;
            chars[closeIndex] = 65537;
        }
        return chars;
    }

    private static int firstMatchingPair(int[] chars, int startIndex, char pairClose, char pairOpen, TextRanges excludedRanges) {
        int openCount = 0;
        for (int i = startIndex; i < chars.length; ++i) {
            if (CopilotStringUtil.isInRange(excludedRanges, i)) continue;
            int c = chars[i];
            if (c == pairOpen) {
                ++openCount;
                continue;
            }
            if (c != pairClose) continue;
            if (openCount == 0) {
                return i;
            }
            --openCount;
        }
        return -1;
    }

    private static boolean isInRange(TextRanges ranges, int i) {
        for (TextRange range : ranges) {
            if (range.contains(i)) {
                return true;
            }
            if (i <= range.getEndOffset()) continue;
            break;
        }
        return false;
    }

    private static TextRanges findStringRanges(int[] chars) {
        TextRanges ranges = new TextRanges();
        int singleQuotedStart = -1;
        int doubleQuotedStart = -1;
        for (int i = 0; i < chars.length; ++i) {
            int c = chars[i];
            if (c == 34 && singleQuotedStart == -1) {
                if (doubleQuotedStart == -1) {
                    doubleQuotedStart = i;
                    continue;
                }
                ranges.union((TextRange)new ProperTextRange(doubleQuotedStart, i));
                doubleQuotedStart = -1;
                continue;
            }
            if (c != 39 || doubleQuotedStart != -1) continue;
            if (singleQuotedStart == -1) {
                singleQuotedStart = i;
                continue;
            }
            ranges.union((TextRange)new ProperTextRange(singleQuotedStart, i));
            singleQuotedStart = -1;
        }
        return ranges;
    }

    static String unpatchCharPairs(int[] patchedData, int offset, int count) {
        int parenChar = 65536;
        int braceChar = 65538;
        int bracketChar = 65540;
        int[] result = new int[count];
        block8: for (int i = 0; i < count; ++i) {
            int c = patchedData[offset + i];
            switch (c) {
                case 65536: {
                    result[i] = 40;
                    continue block8;
                }
                case 65537: {
                    result[i] = 41;
                    continue block8;
                }
                case 65538: {
                    result[i] = 123;
                    continue block8;
                }
                case 65539: {
                    result[i] = 125;
                    continue block8;
                }
                case 65540: {
                    result[i] = 91;
                    continue block8;
                }
                case 65541: {
                    result[i] = 93;
                    continue block8;
                }
                default: {
                    result[i] = c;
                }
            }
        }
        return new String(result, 0, count);
    }

    
}

