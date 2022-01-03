/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.wm.StatusBarWidget
 *  com.intellij.openapi.wm.impl.status.widget.StatusBarEditorBasedWidgetFactory
 *  org.jetbrains.annotations.Nls
 *  org.jetbrains.annotations.NonNls
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.statusBar;

import com.github.copilot.CopilotBundle;
import com.github.copilot.statusBar.CopilotStatusBarWidget;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.impl.status.widget.StatusBarEditorBasedWidgetFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class CopilotWidgetFactory
extends StatusBarEditorBasedWidgetFactory {
    @NonNls
        public String getId() {
        return "com.github.copilot";
    }

    @Nls
        public String getDisplayName() {
        String string = CopilotBundle.get("statusBar.displayName");
        if (string == null) {
            CopilotWidgetFactory.$$$reportNull$$$0(0);
        }
        return string;
    }

        public StatusBarWidget createWidget(Project project) {
        if (project == null) {
            CopilotWidgetFactory.$$$reportNull$$$0(1);
        }
        return new CopilotStatusBarWidget(project);
    }

    public void disposeWidget(StatusBarWidget widget) {
        if (widget == null) {
            CopilotWidgetFactory.$$$reportNull$$$0(2);
        }
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "method %s.%s must not return null";
                break;
            }
            case 1: 
            case 2: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1: 
            case 2: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/statusBar/CopilotWidgetFactory";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "project";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "widget";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getDisplayName";
                break;
            }
            case 1: 
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/statusBar/CopilotWidgetFactory";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: {
                objectArray = objectArray;
                objectArray[2] = "createWidget";
                break;
            }
            case 2: {
                objectArray = objectArray;
                objectArray[2] = "disposeWidget";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
            case 1: 
            case 2: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

