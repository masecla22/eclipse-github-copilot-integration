/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.actionSystem.AnActionEvent
 *  com.intellij.openapi.application.ApplicationManager
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.actions;

import com.github.copilot.actions.CopilotAction;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;

public class CopilotToggleCacheAction
extends AnAction
implements CopilotAction {
    public void actionPerformed(AnActionEvent e) {
        if (e == null) {
            throw new IllegalStateException("e cannot be null!");
        }
        CopilotApplicationSettings.settings().internalDisableHttpCache = !CopilotApplicationSettings.settings().internalDisableHttpCache;
    }

    public void update(AnActionEvent e) {
        if (e == null) {
            throw new IllegalStateException("e cannot be null!");
        }
        e.getPresentation().setEnabledAndVisible(ApplicationManager.getApplication().isInternal());
        if (CopilotApplicationSettings.settings().internalDisableHttpCache) {
            e.getPresentation().setText("Copilot: Enable OpenAI caching");
        } else {
            e.getPresentation().setText("Copilot: Disable OpenAI caching");
        }
    }
}

