/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang.agent;

import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.Nullable;

public final class CopilotAgent {
    @Nullable
    public static volatile Boolean override;
    private static final boolean useAgentServiceTestMode = false;

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

