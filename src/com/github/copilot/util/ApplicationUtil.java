/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.Application
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.diagnostic.Attachment
 *  com.intellij.openapi.diagnostic.RuntimeExceptionWithAttachments
 *  com.intellij.openapi.util.ShutDownTracker
 */
package com.github.copilot.util;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Attachment;
import com.intellij.openapi.diagnostic.RuntimeExceptionWithAttachments;
import com.intellij.openapi.util.ShutDownTracker;

public final class ApplicationUtil {
    private ApplicationUtil() {
    }

    public static void assertIsNonDispatchThread() {
        Application app = ApplicationManager.getApplication();
        if (app.isUnitTestMode() || app.isHeadlessEnvironment()) {
            return;
        }
        if (!app.isDispatchThread()) {
            return;
        }
        if (ShutDownTracker.isShutdownHookRunning()) {
            return;
        }
        throw new RuntimeExceptionWithAttachments("Access from event dispatch thread is not allowed.", new Attachment[0]);
    }
}

