/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.codeInsight.lookup.LookupManager
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.command.CommandProcessor
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.editor.event.BulkAwareDocumentListener
 *  com.intellij.openapi.editor.event.CaretEvent
 *  com.intellij.openapi.editor.event.CaretListener
 *  com.intellij.openapi.editor.event.DocumentEvent
 *  com.intellij.openapi.editor.event.DocumentListener
 *  com.intellij.openapi.editor.event.EditorFactoryEvent
 *  com.intellij.openapi.editor.event.EditorFactoryListener
 *  com.intellij.openapi.editor.ex.util.EditorUtil
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.util.Disposer
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.editor;

import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.editor.CopilotEditorUtil;
import com.github.copilot.editor.InlayDisposeContext;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.BulkAwareDocumentListener;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class CopilotEditorListener
implements EditorFactoryListener {
    private static final Set<String> REJECTED_COMMANDS = Set.of("Typing", "Backspace", "Choose Lookup Item", "Choose Lookup Item Replace");

    public void editorCreated(EditorFactoryEvent event) {
        Editor editor;
        Project project;
        if (event == null) {
            throw new IllegalStateException("event cannot be null!");
        }
        if ((project = (editor = event.getEditor()).getProject()) == null || project.isDisposed() || !CopilotEditorManager.getInstance().isAvailable(editor)) {
            return;
        }
        Disposable editorDisposable = Disposer.newDisposable((String)"copilotEditorListener");
        EditorUtil.disposeWithEditor((Editor)editor, (Disposable)editorDisposable);
        editor.getCaretModel().addCaretListener((CaretListener)new CopilotCaretListener(editor), editorDisposable);
        editor.getDocument().addDocumentListener((DocumentListener)new CopilotDocumentListener(editor), editorDisposable);
    }

    

    private static final class CopilotDocumentListener
    implements BulkAwareDocumentListener {
                private final Editor editor;

        public CopilotDocumentListener(Editor editor) {
            if (editor == null) {
                throw new IllegalStateException("editor cannot be null!");
            }
            this.editor = editor;
        }

        public void documentChangedNonBulk(DocumentEvent event) {
            Project project;
            if (event == null) {
                throw new IllegalStateException("event cannot be null!");
            }
            if ((project = this.editor.getProject()) == null || project.isDisposed()) {
                return;
            }
            if (!CopilotApplicationSettings.isCopilotEnabled(project, this.editor)) {
                return;
            }
            if (!CopilotEditorUtil.isSelectedEditor(this.editor)) {
                return;
            }
            CopilotEditorManager editorManager = CopilotEditorManager.getInstance();
            if (!editorManager.isAvailable(this.editor)) {
                return;
            }
            CommandProcessor commandProcessor = CommandProcessor.getInstance();
            if (commandProcessor.isUndoTransparentActionInProgress()) {
                return;
            }
            String commandName = commandProcessor.getCurrentCommandName();
            if (commandName != null && REJECTED_COMMANDS.contains(commandName)) {
                return;
            }
            int changeOffset = event.getOffset() + event.getNewLength();
            if (this.editor.getCaretModel().getOffset() != changeOffset) {
                return;
            }
            boolean force = event.getOldLength() != event.getNewLength();
            editorManager.editorModified(this.editor, changeOffset, force);
        }

        private static /* synthetic */ void $$$reportNull$$$0(int n) {
            Object[] objectArray;
            Object[] objectArray2;
            Object[] objectArray3 = new Object[3];
            switch (n) {
                default: {
                    objectArray2 = objectArray3;
                    objectArray3[0] = "editor";
                    break;
                }
                case 1: {
                    objectArray2 = objectArray3;
                    objectArray3[0] = "event";
                    break;
                }
            }
            objectArray2[1] = "com/github/copilot/editor/CopilotEditorListener$CopilotDocumentListener";
            switch (n) {
                default: {
                    objectArray = objectArray2;
                    objectArray2[2] = "<init>";
                    break;
                }
                case 1: {
                    objectArray = objectArray2;
                    objectArray2[2] = "documentChangedNonBulk";
                    break;
                }
            }
            throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
        }
    }

    private static final class CopilotCaretListener
    implements CaretListener {
                private final Editor editor;

        public CopilotCaretListener(Editor editor) {
            if (editor == null) {
                throw new IllegalStateException("editor cannot be null!");
            }
            this.editor = editor;
        }

        public void caretPositionChanged(CaretEvent event) {
            Project project;
            if (event == null) {
                throw new IllegalStateException("event cannot be null!");
            }
            if ((project = this.editor.getProject()) == null || project.isDisposed()) {
                return;
            }
            if (CopilotApplicationSettings.settings().isShowIdeCompletions() && LookupManager.getActiveLookup((Editor)this.editor) != null) {
                if (CopilotEditorManager.getInstance().hasCompletionInlays(this.editor)) {
                    CopilotEditorManager.getInstance().editorModified(this.editor);
                }
                return;
            }
            CopilotEditorManager.getInstance().disposeInlays(this.editor, InlayDisposeContext.CaretChange);
        }

        private static /* synthetic */ void $$$reportNull$$$0(int n) {
            Object[] objectArray;
            Object[] objectArray2;
            Object[] objectArray3 = new Object[3];
            switch (n) {
                default: {
                    objectArray2 = objectArray3;
                    objectArray3[0] = "editor";
                    break;
                }
                case 1: {
                    objectArray2 = objectArray3;
                    objectArray3[0] = "event";
                    break;
                }
            }
            objectArray2[1] = "com/github/copilot/editor/CopilotEditorListener$CopilotCaretListener";
            switch (n) {
                default: {
                    objectArray = objectArray2;
                    objectArray2[2] = "<init>";
                    break;
                }
                case 1: {
                    objectArray = objectArray2;
                    objectArray2[2] = "caretPositionChanged";
                    break;
                }
            }
            throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
        }
    }
}

