/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.fileEditor.FileEditorManager
 *  com.intellij.openapi.project.DumbAware
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.wm.ToolWindow
 *  com.intellij.openapi.wm.ToolWindowFactory
 *  com.intellij.openapi.wm.ToolWindowManager
 *  com.intellij.openapi.wm.ex.ToolWindowEx
 *  com.intellij.ui.SimpleTextAttributes
 *  com.intellij.ui.content.ContentManagerEvent
 *  com.intellij.ui.content.ContentManagerListener
 *  com.intellij.util.ui.StatusText
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.toolWindow;

import com.github.copilot.CopilotBundle;
import com.github.copilot.actions.RefreshCopilotToolWindowAction;
import com.github.copilot.editor.CopilotEditorManager;
import com.github.copilot.toolWindow.CopilotSplitEditorManager;
import com.github.copilot.toolWindow.ToolWindowUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.content.ContentManagerEvent;
import com.intellij.ui.content.ContentManagerListener;
import com.intellij.util.ui.StatusText;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpenCopilotToolWindowFactory
implements ToolWindowFactory,
DumbAware {
    static final String ID = "github.copilotToolWindow";

    public void init(ToolWindow toolWindow) {
        Project project;
        if (toolWindow == null) {
            throw new IllegalStateException("toolWindow cannot be null!");
        }
        RefreshCopilotToolWindowAction action = new RefreshCopilotToolWindowAction();
        toolWindow.setTitleActions(Collections.singletonList(action));
        action.registerCustomShortcutSet(toolWindow.getComponent(), toolWindow.getDisposable());
        Project project2 = project = toolWindow instanceof ToolWindowEx ? ((ToolWindowEx)toolWindow).getProject() : null;
        if (project != null) {
            toolWindow.addContentManagerListener(new ContentManagerListener(){

                public void contentRemoved(ContentManagerEvent event) {
                    if (event == null) {
                    	throw new IllegalStateException("event cannot be null");
                    }
                    Editor editor = FileEditorManager.getInstance((Project)project).getSelectedTextEditor();
                    OpenCopilotToolWindowFactory.refreshEmptyText(project, editor);
                }
            });
        }
    }

    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if (toolWindow == null) {
            throw new IllegalStateException("toolWindow cannot be null!");
        }
    }

    public static void editorSelectionChanged(Project project, Editor newEditor) {
        ToolWindow toolWindow;
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if ((toolWindow = ToolWindowManager.getInstance((Project)project).getToolWindow(ID)) == null) {
            return;
        }
        toolWindow.getContentManager().removeAllContents(true);
        OpenCopilotToolWindowFactory.refreshEmptyText(project, newEditor);
    }

    static void refreshEmptyText(Project project, Editor editor) {
        ToolWindow toolWindow;
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if ((toolWindow = ToolWindowManager.getInstance((Project)project).getToolWindow(ID)) == null) {
            return;
        }
        StatusText emptyText = ToolWindowUtil.getEmptyText(toolWindow);
        if (emptyText == null) {
            return;
        }
        emptyText.clear();
        if (editor != null) {
            boolean available = CopilotEditorManager.getInstance().isAvailable(editor);
            if (available) {
                emptyText.setText(CopilotBundle.get("openCopilot.refreshHint"));
                OpenCopilotToolWindowFactory.appendRefreshLine(emptyText, editor);
            } else {
                emptyText.setText(CopilotBundle.get("openCopilot.unavailableForEditor"));
            }
        } else {
            emptyText.setText(CopilotBundle.get("openCopilot.unavailableNoEditor"));
        }
    }

    static void appendRefreshLine(StatusText emptyText, Editor editor) {
        if (emptyText == null) {
            throw new IllegalStateException("emptyText cannot be null!");
        }
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (editor.isDisposed()) {
            return;
        }
        emptyText.appendLine(CopilotBundle.get("openCopilot.refreshLabel"), SimpleTextAttributes.LINK_ATTRIBUTES, e -> CopilotSplitEditorManager.getInstance().openCopilot(editor, true));
    }

    
}

