/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.actionSystem.AnActionEvent
 *  com.intellij.openapi.project.DumbAware
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.actions;

import com.github.copilot.actions.CopilotAction;
import com.github.copilot.github.GitHubService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class LoginToGitHubAction
extends AnAction
implements DumbAware,
CopilotAction {
    public void update(@NotNull AnActionEvent e) {
        if (e == null) {
            LoginToGitHubAction.$$$reportNull$$$0(0);
        }
        e.getPresentation().setEnabled(e.getProject() != null);
    }

    public void actionPerformed(@NotNull AnActionEvent e) {
        if (e == null) {
            LoginToGitHubAction.$$$reportNull$$$0(1);
        }
        GitHubService.getInstance().loginInteractive(Objects.requireNonNull(e.getProject()));
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        objectArray2[0] = "e";
        objectArray2[1] = "com/github/copilot/actions/LoginToGitHubAction";
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
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objectArray));
    }
}

