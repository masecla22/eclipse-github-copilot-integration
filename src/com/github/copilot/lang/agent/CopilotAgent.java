/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang.agent;

import com.intellij.openapi.application.ApplicationManager;

public final class CopilotAgent {
        public static volatile Boolean override;
    private CopilotAgent() {
    }

    public static boolean isAgentSupportedAndEnabled() {
        Boolean currentOverride = override;
        if (currentOverride != null) {
            return currentOverride;
        }
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return false;
        }
        return false;
    }
}

