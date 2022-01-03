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
	public static final String[] DEFAULT_SINGLE_LINE_STOP = new String[] { "\n" };
	public static final String[] DEFAULT_MULTI_LINE_STOP = new String[] { "\n\n\n" };

	public static LanguageSupport find(PsiFile file) {
		if (file == null) {
			throw new IllegalStateException("file cannot be null!");
		}
		return (LanguageSupport) EP.findFirstSafe(e -> e.isAvailable(file));
	}

	public boolean isAvailable(PsiFile var1);

	public FileType getFileType();

	public CopilotLanguage getCopilotLanguage();

	@RequiresBackgroundThread
	@RequiresReadLock
	public boolean isEmptyBlockStart(Project var1, PsiFile var2, int var3);

	@RequiresBackgroundThread
	public Integer findBlockEnd(Project var1, Cancellable var2, String var3, int var4, String var5, boolean var6);

	default public String[] getMultiLineStops() {
		if (DEFAULT_MULTI_LINE_STOP == null) {
			throw new IllegalStateException("DEFAULT_MULTI_LINE_STOP cannot be null");
		}
		return DEFAULT_MULTI_LINE_STOP;
	}

	default public String[] getSingleLineStops() {
		if (DEFAULT_SINGLE_LINE_STOP == null) {
			throw new IllegalStateException("DEFAULT_SINGLE_LINE_STOP cannot be null");
		}
		return DEFAULT_SINGLE_LINE_STOP;
	}

}
