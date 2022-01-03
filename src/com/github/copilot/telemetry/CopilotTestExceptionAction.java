/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.actionSystem.AnActionEvent
 *  com.intellij.openapi.application.ApplicationManager
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.telemetry;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class CopilotTestExceptionAction
extends AnAction {
    public void update(AnActionEvent e) {
        if (e == null) {
            throw new IllegalStateException("e cannot be null!");
        }
        e.getPresentation().setEnabledAndVisible(ApplicationManager.getApplication().isInternal());
    }

    public boolean isDumbAware() {
        return true;
    }

    public void actionPerformed(AnActionEvent e) {
        if (e == null) {
            throw new IllegalStateException("e cannot be null!");
        }
        throw new RuntimeException("Test exception by GitHub Copilot plugin, " + UUID.randomUUID());
    }

    
}

