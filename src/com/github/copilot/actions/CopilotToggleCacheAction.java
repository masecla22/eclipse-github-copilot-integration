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
            CopilotToggleCacheAction.$$$reportNull$$$0(0);
        }
        CopilotApplicationSettings.settings().internalDisableHttpCache = !CopilotApplicationSettings.settings().internalDisableHttpCache;
    }

    public void update(AnActionEvent e) {
        if (e == null) {
            CopilotToggleCacheAction.$$$reportNull$$$0(1);
        }
        e.getPresentation().setEnabledAndVisible(ApplicationManager.getApplication().isInternal());
        if (CopilotApplicationSettings.settings().internalDisableHttpCache) {
            e.getPresentation().setText("Copilot: Enable OpenAI caching");
        } else {
            e.getPresentation().setText("Copilot: Disable OpenAI caching");
        }
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        objectArray2[0] = "e";
        objectArray2[1] = "com/github/copilot/actions/CopilotToggleCacheAction";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "actionPerformed";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[2] = "update";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

