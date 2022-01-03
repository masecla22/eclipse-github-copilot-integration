/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.actionSystem.AnActionEvent
 *  com.intellij.openapi.project.DumbAware
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.ui.Messages
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.actions;

import com.github.copilot.CopilotBundle;
import com.github.copilot.actions.CopilotAction;
import com.github.copilot.github.GitHubService;
import com.github.copilot.status.CopilotStatus;
import com.github.copilot.status.CopilotStatusService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class LogoutFromGitHubAction
extends AnAction
implements DumbAware,
CopilotAction {
    public void update(AnActionEvent e) {
        if (e == null) {
            LogoutFromGitHubAction.$$$reportNull$$$0(0);
        }
        e.getPresentation().setEnabled(GitHubService.getInstance().isSignedIn());
    }

    public void actionPerformed(AnActionEvent e) {
        if (e == null) {
            LogoutFromGitHubAction.$$$reportNull$$$0(1);
        }
        GitHubService.getInstance().logout();
        CopilotStatusService.notifyApplication(CopilotStatus.NotSignedIn);
        Messages.showInfoMessage((Project)e.getProject(), (String)CopilotBundle.get("github.logout.success.message"), (String)CopilotBundle.get("github.logout.success.title"));
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        objectArray2[0] = "e";
        objectArray2[1] = "com/github/copilot/actions/LogoutFromGitHubAction";
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
