/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.util.TextRange
 *  com.intellij.psi.PsiElement
 *  com.intellij.psi.PsiFile
 *  com.intellij.util.concurrency.annotations.RequiresReadLock
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang.prompt;

import com.github.copilot.lang.prompt.LanguageMarker;
import com.github.copilot.lang.prompt.LocalImportContext;
import com.github.copilot.lang.prompt.PathMarker;
import com.github.copilot.lang.prompt.PromptInfo;
import com.github.copilot.lang.prompt.PromptLanguageSupport;
import com.github.copilot.lang.prompt.SiblingOption;
import com.github.copilot.util.CopilotStringUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.concurrency.annotations.RequiresReadLock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PromptUtils {
        @RequiresReadLock
    public static PromptInfo getPrompt(PromptLanguageSupport promptSupport, PsiFile file, String relativeFilePath, String documentContent, int offset) {
        if (promptSupport == null) {
            PromptUtils.$$$reportNull$$$0(0);
        }
        if (file == null) {
            PromptUtils.$$$reportNull$$$0(1);
        }
        if (documentContent == null) {
            PromptUtils.$$$reportNull$$$0(2);
        }
        return PromptUtils.getPrompt(promptSupport, file, relativeFilePath, documentContent, offset, 1500, LanguageMarker.Top, PathMarker.Top, SiblingOption.ContextOverSiblings, LocalImportContext.NoContext);
    }

        public static PromptInfo getPrompt(PromptLanguageSupport promptSupport, PsiFile file, String relativeFilePath, String documentContent, int documentOffset, int maxPromptLength, LanguageMarker languageMarker, PathMarker pathMarker, SiblingOption siblingOption, LocalImportContext importContext) {
        int lastLinefeed;
        Object prompt;
        String marker;
        if (promptSupport == null) {
            PromptUtils.$$$reportNull$$$0(3);
        }
        if (file == null) {
            PromptUtils.$$$reportNull$$$0(4);
        }
        if (documentContent == null) {
            PromptUtils.$$$reportNull$$$0(5);
        }
        if (languageMarker == null) {
            PromptUtils.$$$reportNull$$$0(6);
        }
        if (pathMarker == null) {
            PromptUtils.$$$reportNull$$$0(7);
        }
        if (siblingOption == null) {
            PromptUtils.$$$reportNull$$$0(8);
        }
        if (importContext == null) {
            PromptUtils.$$$reportNull$$$0(9);
        }
        assert (documentOffset <= documentContent.length());
        assert (documentOffset <= file.getTextLength());
        if (documentOffset < 10 && !ApplicationManager.getApplication().isUnitTestMode()) {
            return null;
        }
        int offset = documentOffset;
        StringBuilder doc = new StringBuilder(documentContent);
        StringBuilder promptBuilder = new StringBuilder();
        StringBuilder preSource = new StringBuilder();
        if (languageMarker != LanguageMarker.NoMarker) {
            String string = marker = PromptUtils.hasLanguageMarker(documentContent) ? "" : PromptUtils.withLinefeed(promptSupport.getLanguageMarker(file));
            if (languageMarker == LanguageMarker.Always) {
                promptBuilder.append(marker);
            } else if (languageMarker == LanguageMarker.Top) {
                preSource.append(marker);
            }
        }
        if (pathMarker != PathMarker.NoMarker && relativeFilePath != null && !relativeFilePath.isEmpty()) {
            marker = PromptUtils.withLinefeed(promptSupport.getPathMarker(file.getLanguage(), relativeFilePath));
            if (pathMarker == PathMarker.Always) {
                promptBuilder.append(marker);
            } else if (pathMarker == PathMarker.Top) {
                preSource.append(marker);
            }
        }
        if (siblingOption != SiblingOption.NoSiblings) {
            int tokenLimit = maxPromptLength;
            if (siblingOption == SiblingOption.ContextOverSiblings) {
                tokenLimit = maxPromptLength - PromptUtils.tokenLength(promptBuilder) - PromptUtils.tokenLength(documentContent.subSequence(0, documentOffset));
            }
            int insertedTextLength = PromptUtils.insertSiblingFunctions(promptSupport, doc, offset, file, tokenLimit);
            offset += insertedTextLength;
        }
        int budget = maxPromptLength;
        if ((budget -= PromptUtils.tokenLength((CharSequence)(prompt = PromptUtils.withLinefeed(PromptUtils.takeLastLinesTokens(promptBuilder.toString(), budget))))) > 0) {
            String virtualSource = PromptUtils.withLinefeed(preSource) + doc.substring(0, offset);
            String add = PromptUtils.takeLastLinesTokens(virtualSource, budget);
            if (!add.isEmpty()) {
                prompt = (String)prompt + add;
            } else {
                int lastLineOffset = virtualSource.lastIndexOf(10);
                String lastLine = lastLineOffset != -1 && lastLineOffset < virtualSource.length() ? virtualSource.substring(lastLineOffset + 1) : virtualSource;
                prompt = (String)prompt + PromptUtils.takeLastTokens(lastLine, budget);
            }
        }
        String lastLine = (lastLinefeed = ((String)prompt).lastIndexOf(10)) == -1 || lastLinefeed + 1 == ((String)prompt).length() ? prompt : ((String)prompt).substring(lastLinefeed + 1);
        String trailingWS = "";
        String trimmed = prompt;
        if (lastLine.length() > 0 && CopilotStringUtil.isSpacesOrTabs(lastLine, false)) {
            trailingWS = lastLine;
            trimmed = ((String)prompt).substring(0, lastLinefeed + 1);
        }
        String languageId = promptSupport.getLanguageId(file);
        return new PromptInfo(languageId, trimmed, trailingWS, PromptUtils.tokenLength(trimmed), promptSupport.getBlockMode());
    }

    private static int insertSiblingFunctions(PromptLanguageSupport promptSupport, StringBuilder doc, int offset, PsiFile file, int tokenLimit) {
        if (promptSupport == null) {
            PromptUtils.$$$reportNull$$$0(10);
        }
        if (doc == null) {
            PromptUtils.$$$reportNull$$$0(11);
        }
        if (file == null) {
            PromptUtils.$$$reportNull$$$0(12);
        }
        if (tokenLimit <= 0 || file.getTextLength() == 0) {
            return 0;
        }
        PsiElement function = promptSupport.findParentFunction(file, offset);
        if (function == null) {
            return 0;
        }
        TextRange functionRange = promptSupport.findFunctionRange(function);
        String indent = PromptUtils.findIndent(doc, functionRange.getStartOffset());
        int insertedTokens = 0;
        StringBuilder textToInsert = new StringBuilder();
        PsiElement next = promptSupport.findNextSiblingFunction(function);
        while (next != null) {
            String siblingText;
            String newText;
            int newTokens;
            TextRange range = promptSupport.findFunctionRange(next);
            if (range.getStartOffset() >= offset && insertedTokens + (newTokens = PromptUtils.tokenLength(newText = PromptUtils.withLinefeed(siblingText = doc.substring(range.getStartOffset(), range.getEndOffset())) + "\n" + indent)) <= tokenLimit) {
                textToInsert.append(newText);
                insertedTokens += newTokens;
            }
            next = promptSupport.findNextSiblingFunction(next);
        }
        doc.insert(functionRange.getStartOffset(), textToInsert);
        return textToInsert.length();
    }

        static String findIndent(CharSequence doc, int offset) {
        char c;
        int index;
        if (doc == null) {
            PromptUtils.$$$reportNull$$$0(13);
        }
        if ((index = offset) <= 0) {
            return "";
        }
        while (index > 0 && ((c = doc.charAt(index - 1)) == ' ' || c == '\t')) {
            --index;
        }
        if (index == offset) {
            return "";
        }
        String string = doc.subSequence(index, offset).toString();
        if (string == null) {
            PromptUtils.$$$reportNull$$$0(14);
        }
        return string;
    }

        static String takeLastLinesTokens(String text, int budget) {
        String suffix;
        if (text == null) {
            PromptUtils.$$$reportNull$$$0(15);
        }
        if ((suffix = PromptUtils.takeLastTokens(text, budget)).length() == text.length() || text.charAt(text.length() - suffix.length() - 1) == '\n') {
            String string = suffix;
            if (string == null) {
                PromptUtils.$$$reportNull$$$0(16);
            }
            return string;
        }
        String string = suffix.substring(suffix.indexOf(10) + 1);
        if (string == null) {
            PromptUtils.$$$reportNull$$$0(17);
        }
        return string;
    }

        static String takeLastTokens(String text, int n) {
        if (text == null) {
            PromptUtils.$$$reportNull$$$0(18);
        }
        int nChars = (int)Math.floor((double)n * 2.5);
        int textLength = text.length();
        String string = textLength < nChars ? text : text.substring(textLength - nChars, textLength);
        if (string == null) {
            PromptUtils.$$$reportNull$$$0(19);
        }
        return string;
    }

    public static int tokenLength(CharSequence text) {
        if (text == null) {
            PromptUtils.$$$reportNull$$$0(20);
        }
        return (int)Math.ceil((double)text.length() / 2.5);
    }

    static boolean hasLanguageMarker(String source) {
        if (source == null) {
            PromptUtils.$$$reportNull$$$0(21);
        }
        return source.startsWith("#!") || source.startsWith("<!DOCTYPE");
    }

        static String withLinefeed(String text) {
        if (text == null) {
            PromptUtils.$$$reportNull$$$0(22);
        }
        if (text.isEmpty() || text.endsWith("\n")) {
            String string = text;
            if (string == null) {
                PromptUtils.$$$reportNull$$$0(23);
            }
            return string;
        }
        String string = text + "\n";
        if (string == null) {
            PromptUtils.$$$reportNull$$$0(24);
        }
        return string;
    }

        static String withLinefeed(CharSequence text) {
        int length;
        if (text == null) {
            PromptUtils.$$$reportNull$$$0(25);
        }
        if ((length = text.length()) == 0 || text.charAt(length - 1) == '\n') {
            String string = text.toString();
            if (string == null) {
                PromptUtils.$$$reportNull$$$0(26);
            }
            return string;
        }
        String string = text + "\n";
        if (string == null) {
            PromptUtils.$$$reportNull$$$0(27);
        }
        return string;
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
            case 14: 
            case 16: 
            case 17: 
            case 19: 
            case 23: 
            case 24: 
            case 26: 
            case 27: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 14: 
            case 16: 
            case 17: 
            case 19: 
            case 23: 
            case 24: 
            case 26: 
            case 27: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "promptSupport";
                break;
            }
            case 1: 
            case 4: 
            case 12: {
                objectArray2 = objectArray3;
                objectArray3[0] = "file";
                break;
            }
            case 2: 
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "documentContent";
                break;
            }
            case 6: {
                objectArray2 = objectArray3;
                objectArray3[0] = "languageMarker";
                break;
            }
            case 7: {
                objectArray2 = objectArray3;
                objectArray3[0] = "pathMarker";
                break;
            }
            case 8: {
                objectArray2 = objectArray3;
                objectArray3[0] = "siblingOption";
                break;
            }
            case 9: {
                objectArray2 = objectArray3;
                objectArray3[0] = "importContext";
                break;
            }
            case 11: 
            case 13: {
                objectArray2 = objectArray3;
                objectArray3[0] = "doc";
                break;
            }
            case 14: 
            case 16: 
            case 17: 
            case 19: 
            case 23: 
            case 24: 
            case 26: 
            case 27: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/lang/prompt/PromptUtils";
                break;
            }
            case 15: 
            case 18: 
            case 20: 
            case 22: 
            case 25: {
                objectArray2 = objectArray3;
                objectArray3[0] = "text";
                break;
            }
            case 21: {
                objectArray2 = objectArray3;
                objectArray3[0] = "source";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/lang/prompt/PromptUtils";
                break;
            }
            case 14: {
                objectArray = objectArray2;
                objectArray2[1] = "findIndent";
                break;
            }
            case 16: 
            case 17: {
                objectArray = objectArray2;
                objectArray2[1] = "takeLastLinesTokens";
                break;
            }
            case 19: {
                objectArray = objectArray2;
                objectArray2[1] = "takeLastTokens";
                break;
            }
            case 23: 
            case 24: 
            case 26: 
            case 27: {
                objectArray = objectArray2;
                objectArray2[1] = "withLinefeed";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "getPrompt";
                break;
            }
            case 10: 
            case 11: 
            case 12: {
                objectArray = objectArray;
                objectArray[2] = "insertSiblingFunctions";
                break;
            }
            case 13: {
                objectArray = objectArray;
                objectArray[2] = "findIndent";
                break;
            }
            case 14: 
            case 16: 
            case 17: 
            case 19: 
            case 23: 
            case 24: 
            case 26: 
            case 27: {
                break;
            }
            case 15: {
                objectArray = objectArray;
                objectArray[2] = "takeLastLinesTokens";
                break;
            }
            case 18: {
                objectArray = objectArray;
                objectArray[2] = "takeLastTokens";
                break;
            }
            case 20: {
                objectArray = objectArray;
                objectArray[2] = "tokenLength";
                break;
            }
            case 21: {
                objectArray = objectArray;
                objectArray[2] = "hasLanguageMarker";
                break;
            }
            case 22: 
            case 25: {
                objectArray = objectArray;
                objectArray[2] = "withLinefeed";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 14: 
            case 16: 
            case 17: 
            case 19: 
            case 23: 
            case 24: 
            case 26: 
            case 27: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

