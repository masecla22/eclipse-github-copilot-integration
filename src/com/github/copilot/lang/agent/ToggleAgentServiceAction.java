/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.actionSystem.AnActionEvent
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.project.DumbAware
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent;

import com.github.copilot.lang.agent.CopilotAgent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

public class ToggleAgentServiceAction
extends AnAction
implements DumbAware {
    public void update(AnActionEvent e) {
        if (e == null) {
            ToggleAgentServiceAction.$$$reportNull$$$0(0);
        }
        e.getPresentation().setEnabledAndVisible(ApplicationManager.getApplication().isInternal());
    }

    public void actionPerformed(AnActionEvent e) {
        Boolean current;
        if (e == null) {
            ToggleAgentServiceAction.$$$reportNull$$$0(1);
        }
        CopilotAgent.override = (current = CopilotAgent.override) == null ? Boolean.valueOf(true) : null;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        objectArray2[0] = "e";
        objectArray2[1] = "com/github/copilot/lang/agent/ToggleAgentServiceAction";
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

