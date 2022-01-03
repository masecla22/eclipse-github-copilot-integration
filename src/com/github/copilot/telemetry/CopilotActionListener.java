/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.actionSystem.ActionManager
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.actionSystem.AnActionEvent
 *  com.intellij.openapi.actionSystem.AnActionResult
 *  com.intellij.openapi.actionSystem.ex.AnActionListener
 *  com.intellij.openapi.application.ApplicationManager
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.telemetry;

import com.github.copilot.actions.CopilotAction;
import com.github.copilot.telemetry.TelemetryService;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.AnActionResult;
import com.intellij.openapi.actionSystem.ex.AnActionListener;
import com.intellij.openapi.application.ApplicationManager;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class CopilotActionListener
implements AnActionListener {
    public void afterActionPerformed(AnAction action, AnActionEvent event, AnActionResult result) {
        if (action == null) {
            throw new IllegalStateException("action cannot be null!");
        }
        if (event == null) {
            throw new IllegalStateException("event cannot be null!");
        }
        if (result == null) {
            throw new IllegalStateException("result cannot be null!");
        }
        if (!(action instanceof CopilotAction)) {
            return;
        }
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }
        Map<String, Double> metrics = Map.of();
        if (event.getInputEvent() != null) {
            metrics = Map.of("runningTimeMs", (double)System.currentTimeMillis() - (double)event.getInputEvent().getWhen());
        }
        String id = ActionManager.getInstance().getId(action);
        boolean isError = !result.isPerformed();
        TelemetryService.getInstance().track("command.executed", Map.of("command", id, "isError", String.valueOf(isError)), metrics);
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[0] = "action";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[0] = "event";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[0] = "result";
                break;
            }
        }
        objectArray[1] = "com/github/copilot/telemetry/CopilotActionListener";
        objectArray[2] = "afterActionPerformed";
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

