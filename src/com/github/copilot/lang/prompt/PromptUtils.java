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

public class PromptUtils {
	@RequiresReadLock
	public static PromptInfo getPrompt(PromptLanguageSupport promptSupport, PsiFile file, String relativeFilePath,
			String documentContent, int offset) {
		if (promptSupport == null) {
			throw new IllegalStateException("promptSupport cannot be null!");
		}
		if (file == null) {
			throw new IllegalStateException("file cannot be null!");
		}
		if (documentContent == null) {
			throw new IllegalStateException("documentContent cannot be null!");
		}
		return PromptUtils.getPrompt(promptSupport, file, relativeFilePath, documentContent, offset, 1500,
				LanguageMarker.Top, PathMarker.Top, SiblingOption.ContextOverSiblings, LocalImportContext.NoContext);
	}

	public static PromptInfo getPrompt(PromptLanguageSupport promptSupport, PsiFile file, String relativeFilePath,
			String documentContent, int documentOffset, int maxPromptLength, LanguageMarker languageMarker,
			PathMarker pathMarker, SiblingOption siblingOption, LocalImportContext importContext) {
		int lastLinefeed;
		Object prompt;
		String marker;
		if (promptSupport == null) {
			throw new IllegalStateException("promptSupport cannot be null!");
		}
		if (file == null) {
			throw new IllegalStateException("file cannot be null!");
		}
		if (documentContent == null) {
			throw new IllegalStateException("documentContent cannot be null!");
		}
		if (languageMarker == null) {
			throw new IllegalStateException("languageMarker cannot be null!");
		}
		if (pathMarker == null) {
			throw new IllegalStateException("pathMarker cannot be null!");
		}
		if (siblingOption == null) {
			throw new IllegalStateException("siblingOption cannot be null!");
		}
		if (importContext == null) {
			throw new IllegalStateException("importContext cannot be null!");
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
			String string = marker = PromptUtils.hasLanguageMarker(documentContent) ? ""
					: PromptUtils.withLinefeed(promptSupport.getLanguageMarker(file));
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
				tokenLimit = maxPromptLength - PromptUtils.tokenLength(promptBuilder)
						- PromptUtils.tokenLength(documentContent.subSequence(0, documentOffset));
			}
			int insertedTextLength = PromptUtils.insertSiblingFunctions(promptSupport, doc, offset, file, tokenLimit);
			offset += insertedTextLength;
		}
		int budget = maxPromptLength;
		if ((budget -= PromptUtils.tokenLength((CharSequence) (prompt = PromptUtils
				.withLinefeed(PromptUtils.takeLastLinesTokens(promptBuilder.toString(), budget))))) > 0) {
			String virtualSource = PromptUtils.withLinefeed(preSource) + doc.substring(0, offset);
			String add = PromptUtils.takeLastLinesTokens(virtualSource, budget);
			if (!add.isEmpty()) {
				prompt = (String) prompt + add;
			} else {
				int lastLineOffset = virtualSource.lastIndexOf(10);
				String lastLine = lastLineOffset != -1 && lastLineOffset < virtualSource.length()
						? virtualSource.substring(lastLineOffset + 1)
						: virtualSource;
				prompt = (String) prompt + PromptUtils.takeLastTokens(lastLine, budget);
			}
		}
		String lastLine = (lastLinefeed = ((String) prompt).lastIndexOf(10)) == -1
				|| lastLinefeed + 1 == ((String) prompt).length() ? prompt
						: ((String) prompt).substring(lastLinefeed + 1);
		String trailingWS = "";
		String trimmed = prompt;
		if (lastLine.length() > 0 && CopilotStringUtil.isSpacesOrTabs(lastLine, false)) {
			trailingWS = lastLine;
			trimmed = ((String) prompt).substring(0, lastLinefeed + 1);
		}
		String languageId = promptSupport.getLanguageId(file);
		return new PromptInfo(languageId, trimmed, trailingWS, PromptUtils.tokenLength(trimmed),
				promptSupport.getBlockMode());
	}

	private static int insertSiblingFunctions(PromptLanguageSupport promptSupport, StringBuilder doc, int offset,
			PsiFile file, int tokenLimit) {
		if (promptSupport == null) {
			throw new IllegalStateException("promptSupport cannot be null!");
		}
		if (doc == null) {
			throw new IllegalStateException("doc cannot be null!");
		}
		if (file == null) {
			throw new IllegalStateException("file cannot be null!");
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
			if (range.getStartOffset() >= offset
					&& insertedTokens + (newTokens = PromptUtils.tokenLength(newText = PromptUtils
							.withLinefeed(siblingText = doc.substring(range.getStartOffset(), range.getEndOffset()))
							+ "\n" + indent)) <= tokenLimit) {
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
			throw new IllegalStateException("doc cannot be null!");
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
			throw new IllegalStateException("string cannot be null!");
		}
		return string;
	}

	static String takeLastLinesTokens(String text, int budget) {
		String suffix;
		if (text == null) {
			throw new IllegalStateException("text cannot be null!");
		}
		if ((suffix = PromptUtils.takeLastTokens(text, budget)).length() == text.length()
				|| text.charAt(text.length() - suffix.length() - 1) == '\n') {
			String string = suffix;
			return string;
		}
		String string = suffix.substring(suffix.indexOf(10) + 1);
		if (string == null) {
			throw new IllegalStateException("string cannot be null!");
		}
		return string;
	}

	static String takeLastTokens(String text, int n) {
		if (text == null) {
			throw new IllegalStateException("text cannot be null!");
		}
		int nChars = (int) Math.floor(n * 2.5);
		int textLength = text.length();
		String string = textLength < nChars ? text : text.substring(textLength - nChars, textLength);
		if (string == null) {
			throw new IllegalStateException("string cannot be null!");
		}
		return string;
	}

	public static int tokenLength(CharSequence text) {
		if (text == null) {
			throw new IllegalStateException("text cannot be null!");
		}
		return (int) Math.ceil(text.length() / 2.5);
	}

	static boolean hasLanguageMarker(String source) {
		if (source == null) {
			throw new IllegalStateException("source cannot be null!");
		}
		return source.startsWith("#!") || source.startsWith("<!DOCTYPE");
	}

	static String withLinefeed(String text) {
		if (text == null) {
			throw new IllegalStateException("text cannot be null!");
		}
		if (text.isEmpty() || text.endsWith("\n")) {
			String string = text;
			return string;
		}
		String string = text + "\n";
		return string;
	}

	static String withLinefeed(CharSequence text) {
		int length;
		if (text == null) {
			throw new IllegalStateException("text cannot be null!");
		}
		if ((length = text.length()) == 0 || text.charAt(length - 1) == '\n') {
			String string = text.toString();
			if (string == null) {
				throw new IllegalStateException("string cannot be null!");
			}
			return string;
		}
		String string = text + "\n";
		return string;
	}

}
