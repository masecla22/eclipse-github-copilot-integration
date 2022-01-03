/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.lang.Language
 *  com.intellij.openapi.extensions.ExtensionPointName
 *  com.intellij.openapi.util.TextRange
 *  com.intellij.psi.PsiElement
 *  com.intellij.psi.PsiFile
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang.prompt;

import com.github.copilot.openai.CopilotLanguage;
import com.github.copilot.request.BlockMode;
import com.intellij.lang.Language;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PromptLanguageSupport {
    public static final ExtensionPointName<PromptLanguageSupport> EP = new ExtensionPointName("com.github.copilot.prompt");

        public static PromptLanguageSupport find(CopilotLanguage language) {
        if (language == null) {
            throw new IllegalStateException("language cannot be null!");
        }
        return (PromptLanguageSupport)EP.findFirstSafe(e -> e.isAvailable(language));
    }

    public boolean isAvailable(CopilotLanguage var1);

        public String getLanguageMarker(PsiFile var1);

        public String getLanguageId(PsiFile var1);

        public String getPathMarker(Language var1, String var2);

        public PsiElement findParentFunction(PsiFile var1, int var2);

        public TextRange findFunctionRange(PsiElement var1);

        public PsiElement findNextSiblingFunction(PsiElement var1);

    public boolean isSupportingMultilineCompletion(Language var1);

        default public BlockMode getBlockMode() {
        BlockMode blockMode = BlockMode.Client;
        if (blockMode == null) {
            throw new IllegalStateException("blockMode cannot be null!");
        }
        return blockMode;
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
            case 1: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 1: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "language";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/lang/prompt/PromptLanguageSupport";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/lang/prompt/PromptLanguageSupport";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "getBlockMode";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "find";
                break;
            }
            case 1: {
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 1: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

