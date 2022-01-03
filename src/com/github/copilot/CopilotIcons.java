/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.util.IconLoader
 */
package com.github.copilot;

import com.intellij.openapi.util.IconLoader;
import javax.swing.Icon;

public final class CopilotIcons {
    public static final Icon COPILOT;
    public static final Icon StatusBarIcon;
    public static final Icon StatusBarIconDisabled;
    public static final Icon StatusBarIconError;
    public static final Icon ToolWindowIcon;

    private CopilotIcons() {
    }

    static {
        StatusBarIcon = COPILOT = IconLoader.getIcon((String)"icons/copilot.svg", CopilotIcons.class);
        StatusBarIconDisabled = IconLoader.getIcon((String)"icons/copilot_disabled.svg", CopilotIcons.class);
        StatusBarIconError = IconLoader.getIcon((String)"icons/copilot_error.svg", CopilotIcons.class);
        ToolWindowIcon = IconLoader.getIcon((String)"icons/copilot_toolwindow.svg", CopilotIcons.class);
    }
}

