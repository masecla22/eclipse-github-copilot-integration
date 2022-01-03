/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.actionSystem.AnActionEvent
 *  com.intellij.openapi.actionSystem.CommonDataKeys
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.wm.StatusBar
 *  com.intellij.openapi.wm.WindowManager
 *  com.intellij.psi.PsiFile
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.actions;

import com.github.copilot.CopilotBundle;
import com.github.copilot.actions.CopilotAction;
import com.github.copilot.lang.fallback.VSCodeLanguageMap;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.github.copilot.settings.CopilotApplicationState;
import com.github.copilot.statusBar.CopilotStatusBarWidget;
import com.github.copilot.telemetry.TelemetryData;
import com.github.copilot.telemetry.TelemetryService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiFile;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class EnableCopilotCompletionsAction
extends AnAction
implements CopilotAction {
    public void update(AnActionEvent e) {
        Project project;
        if (e == null) {
            EnableCopilotCompletionsAction.$$$reportNull$$$0(0);
        }
        if ((project = e.getProject()) == null) {
            e.getPresentation().setEnabledAndVisible(false);
            return;
        }
        PsiFile file = (PsiFile)e.getData(CommonDataKeys.PSI_FILE);
        boolean currentlyDisabled = !CopilotApplicationSettings.settings().enableCompletions || file != null && !CopilotApplicationSettings.isCopilotEnabled(file);
        e.getPresentation().setEnabledAndVisible(currentlyDisabled);
    }

    public void actionPerformed(AnActionEvent e) {
        StatusBar bar;
        Project project;
        if (e == null) {
            EnableCopilotCompletionsAction.$$$reportNull$$$0(1);
        }
        if ((project = e.getProject()) == null || project.isDisposed()) {
            return;
        }
        CopilotApplicationState settings = CopilotApplicationSettings.settings();
        boolean global = !settings.enableCompletions;
        settings.enableCompletions = true;
        PsiFile file = (PsiFile)e.getData(CommonDataKeys.PSI_FILE);
        if (file != null) {
            settings.enableLanguage(file.getLanguage());
        }
        if ((bar = WindowManager.getInstance().getStatusBar(project)) != null) {
            bar.setInfo(CopilotBundle.get("action.copilot.enableCopilot.statusEnabled"));
        }
        CopilotStatusBarWidget.update(project);
        String languageId = file == null ? "*" : VSCodeLanguageMap.INTELLIJ_VSCODE_MAP.getOrDefault(file.getLanguage().getID(), file.getLanguage().getID());
        TelemetryData telemetryData = TelemetryData.createIssued(Map.of("languageId", languageId));
        TelemetryService.getInstance().track("statusBar" + (global ? ".globalOn" : ".languageOn"), telemetryData);
    }

    public boolean isDumbAware() {
        return true;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        objectArray2[0] = "e";
        objectArray2[1] = "com/github/copilot/actions/EnableCopilotCompletionsAction";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "update";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[2] = "actionPerformed";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

