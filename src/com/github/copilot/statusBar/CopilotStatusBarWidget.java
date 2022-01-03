/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.actionSystem.ActionGroup
 *  com.intellij.openapi.actionSystem.ActionManager
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.actionSystem.DataContext
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.ui.popup.JBPopupFactory
 *  com.intellij.openapi.ui.popup.JBPopupFactory$ActionSelectionAid
 *  com.intellij.openapi.ui.popup.ListPopup
 *  com.intellij.openapi.vfs.VirtualFile
 *  com.intellij.openapi.wm.StatusBarWidget
 *  com.intellij.openapi.wm.WindowManager
 *  com.intellij.openapi.wm.impl.status.EditorBasedStatusBarPopup
 *  com.intellij.openapi.wm.impl.status.EditorBasedStatusBarPopup$WidgetState
 *  com.intellij.psi.PsiFile
 *  com.intellij.psi.PsiManager
 *  org.jetbrains.annotations.NonNls
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.statusBar;

import com.github.copilot.CopilotBundle;
import com.github.copilot.CopilotIcons;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.github.copilot.status.CopilotStatus;
import com.github.copilot.status.CopilotStatusService;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.impl.status.EditorBasedStatusBarPopup;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CopilotStatusBarWidget
extends EditorBasedStatusBarPopup {
    private static final String WIDGET_ID = "com.github.copilotWidget";

    public static void update(Project project) {
        StatusBarWidget widget;
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if ((widget = WindowManager.getInstance().getStatusBar(project).getWidget(WIDGET_ID)) instanceof CopilotStatusBarWidget) {
            ((CopilotStatusBarWidget)widget).update(() -> WindowManager.getInstance().getStatusBar(project).updateWidget(WIDGET_ID));
        }
    }

    public CopilotStatusBarWidget(Project project) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        super(project, false);
    }

    @NonNls
        public String ID() {
        return WIDGET_ID;
    }

        protected EditorBasedStatusBarPopup.WidgetState getWidgetState(VirtualFile file) {
        CopilotStatus status = CopilotStatusService.getCurrentStatus();
        if (status != CopilotStatus.Ready) {
            String toolTip = CopilotBundle.get("statusBar.tooltipForError", status.getPresentableText());
            EditorBasedStatusBarPopup.WidgetState state = new EditorBasedStatusBarPopup.WidgetState(toolTip, "", true);
            state.setIcon(CopilotIcons.StatusBarIconError);
            EditorBasedStatusBarPopup.WidgetState widgetState = state;
            if (widgetState == null) {
                throw new IllegalStateException("widgetState cannot be null!");
            }
            return widgetState;
        }
        if (file == null) {
            EditorBasedStatusBarPopup.WidgetState widgetState = EditorBasedStatusBarPopup.WidgetState.HIDDEN;
            if (widgetState == null) {
                throw new IllegalStateException("widgetState cannot be null!");
            }
            return widgetState;
        }
        Boolean enabled = this.isCopilotEnabled(file);
        if (enabled == null) {
            EditorBasedStatusBarPopup.WidgetState widgetState = EditorBasedStatusBarPopup.WidgetState.HIDDEN;
            if (widgetState == null) {
                throw new IllegalStateException("widgetState cannot be null!");
            }
            return widgetState;
        }
        String toolTip = CopilotBundle.get(enabled != false ? "statusBar.tooltipForEnabled" : "statusBar.tooltipForDisabled");
        EditorBasedStatusBarPopup.WidgetState state = new EditorBasedStatusBarPopup.WidgetState(toolTip, "", true);
        state.setIcon(enabled != false ? CopilotIcons.StatusBarIcon : CopilotIcons.StatusBarIconDisabled);
        EditorBasedStatusBarPopup.WidgetState widgetState = state;
        if (widgetState == null) {
            throw new IllegalStateException("widgetState cannot be null!");
        }
        return widgetState;
    }

        protected ListPopup createPopup(DataContext context) {
        String groupId = CopilotStatusService.getCurrentStatus() == CopilotStatus.Ready ? "copilot.statusBarPopup" : "copilot.statusBarErrorPopup";
        AnAction group = ActionManager.getInstance().getAction(groupId);
        if (!(group instanceof ActionGroup)) {
            return null;
        }
        return JBPopupFactory.getInstance().createActionGroupPopup(CopilotBundle.get("statusBar.displayName"), (ActionGroup)group, context, JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false);
    }

        protected StatusBarWidget createInstance(Project project) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        return new CopilotStatusBarWidget(project);
    }

        private Boolean isCopilotEnabled(VirtualFile file) {
        PsiFile psiFile;
        if (file == null) {
            throw new IllegalStateException("file cannot be null!");
        }
        if ((psiFile = PsiManager.getInstance((Project)this.myProject).findFile(file)) == null) {
            return null;
        }
        return CopilotApplicationSettings.isCopilotEnabled(psiFile.getLanguage());
    }

    
}

