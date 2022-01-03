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
            CopilotStatusBarWidget.$$$reportNull$$$0(0);
        }
        if ((widget = WindowManager.getInstance().getStatusBar(project).getWidget(WIDGET_ID)) instanceof CopilotStatusBarWidget) {
            ((CopilotStatusBarWidget)widget).update(() -> WindowManager.getInstance().getStatusBar(project).updateWidget(WIDGET_ID));
        }
    }

    public CopilotStatusBarWidget(Project project) {
        if (project == null) {
            CopilotStatusBarWidget.$$$reportNull$$$0(1);
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
                CopilotStatusBarWidget.$$$reportNull$$$0(2);
            }
            return widgetState;
        }
        if (file == null) {
            EditorBasedStatusBarPopup.WidgetState widgetState = EditorBasedStatusBarPopup.WidgetState.HIDDEN;
            if (widgetState == null) {
                CopilotStatusBarWidget.$$$reportNull$$$0(3);
            }
            return widgetState;
        }
        Boolean enabled = this.isCopilotEnabled(file);
        if (enabled == null) {
            EditorBasedStatusBarPopup.WidgetState widgetState = EditorBasedStatusBarPopup.WidgetState.HIDDEN;
            if (widgetState == null) {
                CopilotStatusBarWidget.$$$reportNull$$$0(4);
            }
            return widgetState;
        }
        String toolTip = CopilotBundle.get(enabled != false ? "statusBar.tooltipForEnabled" : "statusBar.tooltipForDisabled");
        EditorBasedStatusBarPopup.WidgetState state = new EditorBasedStatusBarPopup.WidgetState(toolTip, "", true);
        state.setIcon(enabled != false ? CopilotIcons.StatusBarIcon : CopilotIcons.StatusBarIconDisabled);
        EditorBasedStatusBarPopup.WidgetState widgetState = state;
        if (widgetState == null) {
            CopilotStatusBarWidget.$$$reportNull$$$0(5);
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
            CopilotStatusBarWidget.$$$reportNull$$$0(6);
        }
        return new CopilotStatusBarWidget(project);
    }

        private Boolean isCopilotEnabled(VirtualFile file) {
        PsiFile psiFile;
        if (file == null) {
            CopilotStatusBarWidget.$$$reportNull$$$0(7);
        }
        if ((psiFile = PsiManager.getInstance((Project)this.myProject).findFile(file)) == null) {
            return null;
        }
        return CopilotApplicationSettings.isCopilotEnabled(psiFile.getLanguage());
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
            case 2: 
            case 3: 
            case 4: 
            case 5: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "project";
                break;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/statusBar/CopilotStatusBarWidget";
                break;
            }
            case 7: {
                objectArray2 = objectArray3;
                objectArray3[0] = "file";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/statusBar/CopilotStatusBarWidget";
                break;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: {
                objectArray = objectArray2;
                objectArray2[1] = "getWidgetState";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "update";
                break;
            }
            case 1: {
                objectArray = objectArray;
                objectArray[2] = "<init>";
                break;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: {
                break;
            }
            case 6: {
                objectArray = objectArray;
                objectArray[2] = "createInstance";
                break;
            }
            case 7: {
                objectArray = objectArray;
                objectArray[2] = "isCopilotEnabled";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

