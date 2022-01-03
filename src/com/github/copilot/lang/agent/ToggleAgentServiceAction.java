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

public class ToggleAgentServiceAction
extends AnAction
implements DumbAware {
    public void update(AnActionEvent e) {
        if (e == null) {
            throw new IllegalStateException("e cannot be null!");
        }
        e.getPresentation().setEnabledAndVisible(ApplicationManager.getApplication().isInternal());
    }

    public void actionPerformed(AnActionEvent e) {
        Boolean current;
        if (e == null) {
            throw new IllegalStateException("e cannot be null!");
        }
        CopilotAgent.override = (current = CopilotAgent.override) == null ? Boolean.valueOf(true) : null;
    }

    
}

