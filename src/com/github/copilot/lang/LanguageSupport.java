/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.extensions.ExtensionPointName
 *  com.intellij.openapi.fileTypes.FileType
 *  com.intellij.openapi.project.Project
 *  com.intellij.psi.PsiFile
 *  com.intellij.util.concurrency.annotations.RequiresBackgroundThread
 *  com.intellij.util.concurrency.annotations.RequiresReadLock
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang;

import com.github.copilot.openai.CopilotLanguage;
import com.github.copilot.util.Cancellable;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import com.intellij.util.concurrency.annotations.RequiresReadLock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LanguageSupport {
    public static final ExtensionPointName<LanguageSupport> EP = new ExtensionPointName("com.github.copilot.language");
    public static final String[] DEFAULT_SINGLE_LINE_STOP = new String[]{"\n"};
    public static final String[] DEFAULT_MULTI_LINE_STOP = new String[]{"\n\n\n"};

    @Nullable
    public static LanguageSupport find(@NotNull PsiFile file) {
        if (file == null) {
            LanguageSupport.$$$reportNull$$$0(0);
        }
        return (LanguageSupport)EP.findFirstSafe(e -> e.isAvailable(file));
    }

    public boolean isAvailable(@NotNull PsiFile var1);

    @Nullable
    public FileType getFileType();

    @NotNull
    public CopilotLanguage getCopilotLanguage();

    @RequiresBackgroundThread
    @RequiresReadLock
    public boolean isEmptyBlockStart(@NotNull Project var1, @NotNull PsiFile var2, int var3);

    @Nullable
    @RequiresBackgroundThread
    public Integer findBlockEnd(@NotNull Project var1, @NotNull Cancellable var2, @NotNull String var3, int var4, @NotNull String var5, boolean var6);

    @NotNull
    default public String[] getMultiLineStops() {
        if (DEFAULT_MULTI_LINE_STOP == null) {
            LanguageSupport.$$$reportNull$$$0(1);
        }
        return DEFAULT_MULTI_LINE_STOP;
    }

    @NotNull
    default public String[] getSingleLineStops() {
        if (DEFAULT_SINGLE_LINE_STOP == null) {
            LanguageSupport.$$$reportNull$$$0(2);
        }
        return DEFAULT_SINGLE_LINE_STOP;
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
            case 1: 
            case 2: {
                string = "@NotNull method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 1: 
            case 2: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "file";
                break;
            }
            case 1: 
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/lang/LanguageSupport";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/lang/LanguageSupport";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "getMultiLineStops";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "getSingleLineStops";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "find";
                break;
            }
            case 1: 
            case 2: {
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 1: 
            case 2: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

