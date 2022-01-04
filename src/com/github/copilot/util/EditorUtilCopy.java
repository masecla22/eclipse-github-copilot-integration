/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.editor.Document
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.editor.EditorSettings
 *  com.intellij.openapi.editor.actions.EditorActionUtil
 *  com.intellij.openapi.project.Project
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.util;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.actions.EditorActionUtil;
import com.intellij.openapi.project.Project;

public class EditorUtilCopy {
	public static int indentLine(Project project, Editor editor, int lineNumber, int indent, int caretOffset) {
		if (editor == null) {
			throw new IllegalStateException("editor cannot be null!");
		}
		return EditorUtilCopy.indentLine(project, editor, lineNumber, indent, caretOffset,
				EditorActionUtil.shouldUseSmartTabs((Project) project, (Editor) editor));
	}

	public static int indentLine(Project project, Editor editor, int lineNumber, int indent, int caretOffset,
			boolean shouldUseSmartTabs) {
		int newCaretOffset;
		if (editor == null) {
			throw new IllegalStateException("editor cannot be null!");
		}
		EditorSettings editorSettings = editor.getSettings();
		int tabSize = editorSettings.getTabSize(project);
		Document document = editor.getDocument();
		CharSequence text = document.getImmutableCharSequence();
		int spacesEnd = 0;
		int lineStart = 0;
		int lineEnd = 0;
		int tabsEnd = 0;
		if (lineNumber < document.getLineCount()) {
			lineStart = document.getLineStartOffset(lineNumber);
			lineEnd = document.getLineEndOffset(lineNumber);
			boolean inTabs = true;
			for (spacesEnd = lineStart; spacesEnd <= lineEnd && spacesEnd != lineEnd; ++spacesEnd) {
				char c = text.charAt(spacesEnd);
				if (c == '\t')
					continue;
				if (inTabs) {
					inTabs = false;
					tabsEnd = spacesEnd;
				}
				if (c != ' ')
					break;
			}
			if (inTabs) {
				tabsEnd = lineEnd;
			}
		}
		if ((newCaretOffset = caretOffset) >= lineStart && newCaretOffset < lineEnd && spacesEnd == lineEnd) {
			spacesEnd = newCaretOffset;
			tabsEnd = Math.min(spacesEnd, tabsEnd);
		}
		int oldLength = EditorUtilCopy.getSpaceWidthInColumns(text, lineStart, spacesEnd, tabSize);
		tabsEnd = EditorUtilCopy.getSpaceWidthInColumns(text, lineStart, tabsEnd, tabSize);
		int newLength = oldLength + indent;
		if (newLength < 0) {
			newLength = 0;
		}
		if ((tabsEnd += indent) < 0) {
			tabsEnd = 0;
		}
		if (!shouldUseSmartTabs) {
			tabsEnd = newLength;
		}
		StringBuilder buf = new StringBuilder(newLength);
		int i = 0;
		while (i < newLength) {
			if (tabSize > 0 && editorSettings.isUseTabCharacter(project) && i + tabSize <= tabsEnd) {
				buf.append('\t');
				i += tabSize;
				continue;
			}
			buf.append(' ');
			++i;
		}
		int newSpacesEnd = lineStart + buf.length();
		if (newCaretOffset >= spacesEnd) {
			newCaretOffset += buf.length() - (spacesEnd - lineStart);
		} else if (newCaretOffset >= lineStart && newCaretOffset > newSpacesEnd) {
			newCaretOffset = newSpacesEnd;
		}
		return newCaretOffset;
	}

	private static int getSpaceWidthInColumns(CharSequence seq, int startOffset, int endOffset, int tabSize) {
		int result = 0;
		for (int i = startOffset; i < endOffset; ++i) {
			if (seq.charAt(i) == '\t') {
				result = (result / tabSize + 1) * tabSize;
				continue;
			}
			++result;
		}
		return result;
	}
}
