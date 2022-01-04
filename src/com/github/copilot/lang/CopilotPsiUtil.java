/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.application.ReadAction
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.fileTypes.FileType
 *  com.intellij.openapi.progress.ProgressManager
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.util.Condition
 *  com.intellij.psi.PsiElement
 *  com.intellij.psi.PsiErrorElement
 *  com.intellij.psi.PsiFile
 *  com.intellij.psi.PsiFileFactory
 *  com.intellij.psi.PsiWhiteSpace
 *  com.intellij.psi.TokenType
 *  com.intellij.psi.impl.source.tree.LeafPsiElement
 *  com.intellij.psi.tree.IElementType
 *  com.intellij.psi.tree.TokenSet
 *  com.intellij.psi.util.PsiTreeUtil
 *  com.intellij.psi.util.PsiUtilCore
 *  com.intellij.util.concurrency.annotations.RequiresBackgroundThread
 *  com.intellij.util.concurrency.annotations.RequiresReadLock
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang;

import com.github.copilot.lang.IsValidBlockFunction;
import com.github.copilot.util.Cancellable;
import com.github.copilot.util.CopilotStringUtil;
import com.github.copilot.util.ObjectToIntFunction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.TokenType;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import com.intellij.util.concurrency.annotations.RequiresReadLock;
import java.util.concurrent.atomic.AtomicReference;

public final class CopilotPsiUtil {
	private static final Logger LOG = Logger.getInstance(CopilotPsiUtil.class);
	private static final int MAX_PARSE_ATTEMPTS = 5;

	private CopilotPsiUtil() {
	}

	public static boolean isWhitespace(PsiElement element) {
		if (element == null) {
			throw new IllegalStateException("element cannot be null!");
		}
		return element instanceof PsiWhiteSpace
				|| TokenType.WHITE_SPACE.equals(PsiUtilCore.getElementType((PsiElement) element));
	}

	public static boolean isMatching(PsiElement element, TokenSet types) {
		if (element == null) {
			throw new IllegalStateException("element cannot be null!");
		}
		if (types == null) {
			throw new IllegalStateException("types cannot be null!");
		}
		return types.contains(PsiUtilCore.getElementType((PsiElement) element));
	}

	public static boolean isMatching(PsiElement element, IElementType type) {
		if (element == null) {
			throw new IllegalStateException("element cannot be null!");
		}
		if (type == null) {
			throw new IllegalStateException("type cannot be null!");
		}
		return type.equals(PsiUtilCore.getElementType((PsiElement) element));
	}

	public static PsiElement prevNonWhitespaceLeaf(PsiElement element) {
		if (element == null) {
			throw new IllegalStateException("element cannot be null!");
		}
		return CopilotPsiUtil.prevNonWhitespaceLeaf(element, false);
	}

	public static PsiElement prevNonWhitespaceLeaf(PsiElement element, boolean withNewlines) {
		if (element == null) {
			throw new IllegalStateException("element cannot be null!");
		}
		if (CopilotPsiUtil.isWhitespace(element)
				&& !CopilotStringUtil.isSpacesOrTabs(element.getText(), withNewlines)) {
			return null;
		}
		PsiElement leaf = PsiTreeUtil.prevLeaf((PsiElement) element, (boolean) true);
		while (leaf != null && CopilotStringUtil.isSpacesOrTabs(leaf.getText(), withNewlines)) {
			leaf = PsiTreeUtil.prevLeaf((PsiElement) leaf, (boolean) true);
		}
		if (leaf != null && CopilotStringUtil.isSpacesOrTabs(leaf.getText(), withNewlines)) {
			return null;
		}
		return leaf;
	}

	public static PsiElement nextNonWhitespaceLeaf(PsiElement element) {
		if (element == null) {
			throw new IllegalStateException("element cannot be null!");
		}
		return CopilotPsiUtil.nextNonWhitespaceLeaf(element, false);
	}

	public static PsiElement nextNonWhitespaceLeaf(PsiElement element, boolean withNewlines) {
		if (element == null) {
			throw new IllegalStateException("element cannot be null!");
		}
		if (CopilotPsiUtil.isWhitespace(element)
				&& !CopilotStringUtil.isSpacesOrTabs(element.getText(), withNewlines)) {
			return null;
		}
		PsiElement leaf = PsiTreeUtil.nextLeaf((PsiElement) element, (boolean) true);
		if (leaf == null || !CopilotStringUtil.isSpacesOrTabs(leaf.getText(), false)) {
			return null;
		}
		while (leaf != null && CopilotStringUtil.isSpacesOrTabs(leaf.getText(), false)) {
			leaf = PsiTreeUtil.nextLeaf((PsiElement) leaf, (boolean) true);
		}
		return leaf;
	}

	public static boolean isAtEndOfLineLeaf(PsiElement element, int relativeOffset, boolean allowTrailingWhitespace) {
		if (element == null) {
			throw new IllegalStateException("element cannot be null!");
		}
		if (!(element instanceof LeafPsiElement)) {
			return false;
		}
		int delta = relativeOffset;
		PsiElement leaf = element;
		while (leaf != null) {
			String text = leaf.getText();
			int textLength = text.length();
			for (int i = delta; i < textLength; ++i) {
				char c = text.charAt(i);
				if (allowTrailingWhitespace && (c == ' ' || c == '\t'))
					continue;
				return c == '\n'
						|| i == textLength - 1 && PsiTreeUtil.nextLeaf((PsiElement) leaf, (boolean) true) == null;
			}
			leaf = PsiTreeUtil.nextLeaf((PsiElement) leaf, (boolean) true);
			delta = 0;
		}
		return true;
	}

	public static PsiElement leafsSkipBackwardsUntil(PsiElement element, boolean strict, boolean skipEmpty,
			boolean skipErrors, Condition<? super PsiElement> condition) {
		PsiElement leaf;
		if (element == null) {
			throw new IllegalStateException("element cannot be null!");
		}
		if (condition == null) {
			throw new IllegalStateException("condition cannot be null!");
		}
		PsiElement psiElement = leaf = strict ? PsiTreeUtil.prevLeaf((PsiElement) element) : element;
		while (leaf != null) {
			boolean skip;
			boolean bl = skip = skipEmpty && leaf.getTextLength() == 0 || skipErrors && leaf instanceof PsiErrorElement;
			if (!skip && condition.value((Object) leaf))
				break;
			leaf = PsiTreeUtil.prevLeaf((PsiElement) leaf, (boolean) false);
		}
		return leaf;
	}

	/*
	 * WARNING - Removed try catching itself - possible behaviour change.
	 */
	@RequiresBackgroundThread
	public static Integer findBlockEnd(Project project, Cancellable request, FileType fileType, String prefix,
			int prefixOffset, String completion, boolean isEndOfStream, IsValidBlockFunction isValidBlock,
			ObjectToIntFunction<PsiElement> patchResult) {
		if (project == null) {
			throw new IllegalStateException("project cannot be null!");
		}
		if (request == null) {
			throw new IllegalStateException("request cannot be null!");
		}
		if (fileType == null) {
			throw new IllegalStateException("fileType cannot be null!");
		}
		if (prefix == null) {
			throw new IllegalStateException("prefix cannot be null!");
		}
		if (completion == null) {
			throw new IllegalStateException("completion cannot be null!");
		}
		if (isValidBlock == null) {
			throw new IllegalStateException("isValidBlock cannot be null!");
		}
		ProgressManager progressManager = ProgressManager.getInstance();
		long start = System.currentTimeMillis();
		try {
			AtomicReference<Object> result = new AtomicReference<Object>(null);
			for (int attempt = 0; attempt < 5; ++attempt) {
				if (request.isCancelled()) {
					LOG.debug("Skipping parsing (request was cancelled)");
					Integer n = null;
					return n;
				}
				boolean success = ApplicationManager.getApplication().isUnitTestMode()
						? ((Boolean) ReadAction.compute(() -> {
							result.set(CopilotPsiUtil.doFindBlockEnd(project, request, fileType, prefix, prefixOffset,
									completion, isEndOfStream, isValidBlock, patchResult));
							return true;
						})).booleanValue()
						: progressManager.runInReadActionWithWriteActionPriority(
								() -> result.set(CopilotPsiUtil.doFindBlockEnd(project, request, fileType, prefix,
										prefixOffset, completion, isEndOfStream, isValidBlock, patchResult)),
								null);
				if (!success)
					continue;
				LOG.trace("findBlockEnd successful after " + attempt + " attempts");
				Integer n = result.get();
				return n;
			}
			LOG.warn("findBlockEnd failed after 5 attempts");
			Integer n = null;
			return n;
		} finally {
			LOG.debug("findBlockEnd: duration " + (System.currentTimeMillis() - start) + " ms");
		}
	}

	@RequiresReadLock
	private static Integer doFindBlockEnd(Project project, Cancellable request, FileType fileType, String prefix,
			int prefixOffset, String completion, boolean isEndOfStream, IsValidBlockFunction isValidBlock,
			ObjectToIntFunction<PsiElement> patchResult) {
		if (project == null) {
			throw new IllegalStateException("project cannot be null!");
		}
		if (request == null) {
			throw new IllegalStateException("request cannot be null!");
		}
		if (fileType == null) {
			throw new IllegalStateException("fileType cannot be null!");
		}
		if (prefix == null) {
			throw new IllegalStateException("prefix cannot be null!");
		}
		if (completion == null) {
			throw new IllegalStateException("completion cannot be null!");
		}
		if (isValidBlock == null) {
			throw new IllegalStateException("isValidBlock cannot be null!");
		}
		assert (prefixOffset <= prefix.length());
		assert (!completion.isEmpty());
		String filePrefix = prefixOffset == prefix.length() ? prefix : prefix.substring(0, prefixOffset);
		int filePrefixLength = filePrefix.length();
		String fileText = filePrefix + completion;
		int firstNonWhitespaceOffset = filePrefix.length() + CopilotStringUtil.leadingWhitespace(completion).length();
		ProgressManager.checkCanceled();
		if (request.isCancelled()) {
			return null;
		}
		PsiFile file = PsiFileFactory.getInstance((Project) project).createFileFromText(
				"_." + fileType.getDefaultExtension(), fileType, (CharSequence) fileText, 0L, false);
		for (PsiElement element = file.findElementAt(firstNonWhitespaceOffset); element != null; element = element
				.getParent()) {
			ProgressManager.checkCanceled();
			if (request.isCancelled()) {
				return null;
			}
			if (isValidBlock.isValidBlock(element, isEndOfStream)) {
				int offset = patchResult != null ? patchResult.apply(element) : element.getTextRange().getEndOffset();
				int delta = offset - filePrefixLength;
				return delta > 0 ? Integer.valueOf(delta) : null;
			}
			if (element instanceof PsiFile)
				break;
		}
		return null;
	}

}
