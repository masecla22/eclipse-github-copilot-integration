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

    public void init(@NotNull ToolWindow toolWindow) {
        Project project;
        if (toolWindow == null) {
            OpenCopilotToolWindowFactory.$$$reportNull$$$0(0);
        }
        RefreshCopilotToolWindowAction action = new RefreshCopilotToolWindowAction();
        toolWindow.setTitleActions(Collections.singletonList(action));
        action.registerCustomShortcutSet(toolWindow.getComponent(), toolWindow.getDisposable());
        Project project2 = project = toolWindow instanceof ToolWindowEx ? ((ToolWindowEx)toolWindow).getProject() : null;
        if (project != null) {
            toolWindow.addContentManagerListener(new ContentManagerListener(){

                public void contentRemoved(@NotNull ContentManagerEvent event) {
                    if (event == null) {
                        1.$$$reportNull$$$0(0);
                    }
                    Editor editor = FileEditorManager.getInstance((Project)project).getSelectedTextEditor();
                    OpenCopilotToolWindowFactory.refreshEmptyText(project, editor);
                }

                private static /* synthetic */ void $$$reportNull$$$0(int n) {
                    throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "event", "com/github/copilot/toolWindow/OpenCopilotToolWindowFactory$1", "contentRemoved"));
                }
            });
        }
    }

    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        if (project == null) {
            OpenCopilotToolWindowFactory.$$$reportNull$$$0(1);
        }
        if (toolWindow == null) {
            OpenCopilotToolWindowFactory.$$$reportNull$$$0(2);
        }
    }

    public static void editorSelectionChanged(@NotNull Project project, @Nullable Editor newEditor) {
        ToolWindow toolWindow;
        if (project == null) {
            OpenCopilotToolWindowFactory.$$$reportNull$$$0(3);
        }
        if ((toolWindow = ToolWindowManager.getInstance((Project)project).getToolWindow(ID)) == null) {
            return;
        }
        toolWindow.getContentManager().removeAllContents(true);
        OpenCopilotToolWindowFactory.refreshEmptyText(project, newEditor);
    }

    static void refreshEmptyText(@NotNull Project project, @Nullable Editor editor) {
        ToolWindow toolWindow;
        if (project == null) {
            OpenCopilotToolWindowFactory.$$$reportNull$$$0(4);
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

    static void appendRefreshLine(@NotNull StatusText emptyText, @NotNull Editor editor) {
        if (emptyText == null) {
            OpenCopilotToolWindowFactory.$$$reportNull$$$0(5);
        }
        if (editor == null) {
            OpenCopilotToolWindowFactory.$$$reportNull$$$0(6);
        }
        if (editor.isDisposed()) {
            return;
        }
        emptyText.appendLine(CopilotBundle.get("openCopilot.refreshLabel"), SimpleTextAttributes.LINK_ATTRIBUTES, e -> CopilotSplitEditorManager.getInstance().openCopilot(editor, true));
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2;
        Object[] objectArray3 = new Object[3];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "toolWindow";
                break;
            }
            case 1: 
            case 3: 
            case 4: {
                objectArray2 = objectArray3;
                objectArray3[0] = "project";
                break;
            }
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "emptyText";
                break;
            }
            case 6: {
                objectArray2 = objectArray3;
                objectArray3[0] = "editor";
                break;
            }
        }
        objectArray2[1] = "com/github/copilot/toolWindow/OpenCopilotToolWindowFactory";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "init";
                break;
            }
            case 1: 
            case 2: {
                objectArray = objectArray2;
                objectArray2[2] = "createToolWindowContent";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[2] = "editorSelectionChanged";
                break;
            }
            case 4: {
                objectArray = objectArray2;
                objectArray2[2] = "refreshEmptyText";
                break;
            }
            case 5: 
            case 6: {
                objectArray = objectArray2;
                objectArray2[2] = "appendRefreshLine";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objectArray));
    }
}

