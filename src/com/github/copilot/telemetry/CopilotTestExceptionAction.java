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
            CopilotTestExceptionAction.$$$reportNull$$$0(0);
        }
        e.getPresentation().setEnabledAndVisible(ApplicationManager.getApplication().isInternal());
    }

    public boolean isDumbAware() {
        return true;
    }

    public void actionPerformed(AnActionEvent e) {
        if (e == null) {
            CopilotTestExceptionAction.$$$reportNull$$$0(1);
        }
        throw new RuntimeException("Test exception by GitHub Copilot plugin, " + UUID.randomUUID());
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        objectArray2[0] = "e";
        objectArray2[1] = "com/github/copilot/telemetry/CopilotTestExceptionAction";
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

