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
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

        public StatusBarWidget createWidget(Project project) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        return new CopilotStatusBarWidget(project);
    }

    public void disposeWidget(StatusBarWidget widget) {
        if (widget == null) {
            throw new IllegalStateException("widget cannot be null!");
        }
    }

    
}

