/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.editor.Document
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.editor.ex.DocumentEx
 *  com.intellij.openapi.editor.ex.EditorEx
 *  com.intellij.openapi.editor.ex.util.EditorUtil
 *  com.intellij.openapi.fileEditor.FileEditor
 *  com.intellij.openapi.fileEditor.FileEditorManager
 *  com.intellij.openapi.fileEditor.TextEditor
 *  com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.roots.ProjectRootManager
 *  com.intellij.openapi.util.Key
 *  com.intellij.openapi.util.UserDataHolder
 *  com.intellij.openapi.vfs.VfsUtilCore
 *  com.intellij.openapi.vfs.VirtualFile
 *  com.intellij.psi.PsiDocumentManager
 *  com.intellij.psi.PsiFile
 *  com.intellij.util.concurrency.annotations.RequiresEdt
 *  com.intellij.util.containers.ContainerUtil
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.editor;

import com.github.copilot.lang.LanguageSupport;
import com.github.copilot.request.BasicEditorRequest;
import com.github.copilot.request.CompletionType;
import com.github.copilot.request.EditorRequest;
import com.github.copilot.request.LineInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.DocumentEx;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.concurrency.annotations.RequiresEdt;
import com.intellij.util.containers.ContainerUtil;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CopilotEditorUtil {
    static final Key<List<EditorRequest>> KEY_REQUESTS = Key.create((String)"copilot.editorRequests");

    private CopilotEditorUtil() {
    }

    public static boolean isFocusedEditor(Editor editor) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return true;
        }
        return editor.getContentComponent().isFocusOwner();
    }

    public static boolean isSelectedEditor(Editor editor) {
        Project project;
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if ((project = editor.getProject()) == null || project.isDisposed()) {
            return false;
        }
        FileEditorManager editorManager = FileEditorManager.getInstance((Project)project);
        if (editorManager == null) {
            return false;
        }
        if (editorManager instanceof FileEditorManagerImpl) {
            Editor current = ((FileEditorManagerImpl)editorManager).getSelectedTextEditor(true);
            return current != null && current.equals(editor);
        }
        FileEditor current = editorManager.getSelectedEditor();
        return current instanceof TextEditor && editor.equals(((TextEditor)current).getEditor());
    }

    @RequiresEdt
        public static BasicEditorRequest createEditorRequest(Editor editor, int offset, CompletionType completionType) {
        Project project;
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (completionType == null) {
            throw new IllegalStateException("completionType cannot be null!");
        }
        if ((project = editor.getProject()) == null) {
            return null;
        }
        Document document = editor.getDocument();
        PsiFile file = PsiDocumentManager.getInstance((Project)project).getPsiFile(document);
        if (file == null) {
            return null;
        }
        LanguageSupport language = LanguageSupport.find(file);
        if (language == null) {
            return null;
        }
        boolean useTabs = editor.getSettings().isUseTabCharacter(project);
        int tabWidth = editor.getSettings().getTabSize(project);
        String relativePath = CopilotEditorUtil.getRelativeFilePath(project, file);
        LineInfo lineInfo = LineInfo.create(document, offset);
        return new BasicEditorRequest(project, language, completionType, file.getLanguage(), relativePath, document.getText(), offset, lineInfo, useTabs, tabWidth, document instanceof DocumentEx ? Integer.valueOf(((DocumentEx)document).getModificationSequence()) : null);
    }

    public static int whitespacePrefixLength(String lineContent) {
        int i;
        if (lineContent == null) {
            throw new IllegalStateException("lineContent cannot be null!");
        }
        int maxLength = lineContent.length();
        for (i = 0; i < maxLength; ++i) {
            char c = lineContent.charAt(i);
            if (c == ' ' || c == '\t') continue;
            return i;
        }
        return i;
    }

        public static String getRelativeFilePath(Project project, PsiFile file) {
        String path;
        VirtualFile root;
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if (file == null) {
            throw new IllegalStateException("file cannot be null!");
        }
        String relativePath = file.getName();
        VirtualFile vFile = file.getVirtualFile();
        if (vFile != null && (root = ProjectRootManager.getInstance((Project)project).getFileIndex().getContentRootForFile(vFile)) != null && (path = VfsUtilCore.getRelativePath((VirtualFile)vFile, (VirtualFile)root)) != null) {
            relativePath = path;
        }
        String string = relativePath;
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

        public static LanguageSupport findLanguageSupport(Editor editor) {
        Project project;
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if ((project = editor.getProject()) == null || project.isDisposed()) {
            return null;
        }
        if (editor instanceof EditorEx && !editor.getDocument().isWritable()) {
            return null;
        }
        PsiFile psiFile = PsiDocumentManager.getInstance((Project)project).getPsiFile(editor.getDocument());
        if (psiFile == null || !psiFile.isValid() || !psiFile.isWritable()) {
            return null;
        }
        return LanguageSupport.find(psiFile);
    }

    public static void addEditorRequest(Editor editor, EditorRequest request) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (request == null) {
            throw new IllegalStateException("request cannot be null!");
        }
        EditorUtil.disposeWithEditor((Editor)editor, request::cancel);
        if (!KEY_REQUESTS.isIn((UserDataHolder)editor)) {
            KEY_REQUESTS.set((UserDataHolder)editor, (Object)ContainerUtil.createLockFreeCopyOnWriteList());
        }
        ((List)KEY_REQUESTS.getRequired((UserDataHolder)editor)).add(request);
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
            case 7: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 7: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "editor";
                break;
            }
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "completionType";
                break;
            }
            case 4: {
                objectArray2 = objectArray3;
                objectArray3[0] = "lineContent";
                break;
            }
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "project";
                break;
            }
            case 6: {
                objectArray2 = objectArray3;
                objectArray3[0] = "file";
                break;
            }
            case 7: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/editor/CopilotEditorUtil";
                break;
            }
            case 10: {
                objectArray2 = objectArray3;
                objectArray3[0] = "request";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/editor/CopilotEditorUtil";
                break;
            }
            case 7: {
                objectArray = objectArray2;
                objectArray2[1] = "getRelativeFilePath";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "isFocusedEditor";
                break;
            }
            case 1: {
                objectArray = objectArray;
                objectArray[2] = "isSelectedEditor";
                break;
            }
            case 2: 
            case 3: {
                objectArray = objectArray;
                objectArray[2] = "createEditorRequest";
                break;
            }
            case 4: {
                objectArray = objectArray;
                objectArray[2] = "whitespacePrefixLength";
                break;
            }
            case 5: 
            case 6: {
                objectArray = objectArray;
                objectArray[2] = "getRelativeFilePath";
                break;
            }
            case 7: {
                break;
            }
            case 8: {
                objectArray = objectArray;
                objectArray[2] = "findLanguageSupport";
                break;
            }
            case 9: 
            case 10: {
                objectArray = objectArray;
                objectArray[2] = "addEditorRequest";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 7: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

