/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.CommonBundle
 *  com.intellij.ide.BrowserUtil
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.ui.Messages
 *  com.intellij.openapi.util.text.StringUtil
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.github;

import com.github.copilot.CopilotBundle;
import com.intellij.CommonBundle;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GitHubAuthUtil {
    private static final String COPILOT_URL = "https://copilot.github.com";

    private GitHubAuthUtil() {
    }

    public static void showUnauthorizedMessage(Project project, String url) {
        int clickedButton;
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if ((clickedButton = Messages.showDialog((Project)project, (String)CopilotBundle.get("github.login.copilotUnauthorized.message", StringUtil.defaultIfEmpty((String)url, (String)COPILOT_URL)), (String)CopilotBundle.get("github.login.copilotUnauthorized.title"), (String[])new String[]{CopilotBundle.get("github.login.copilotUnauthorized.joinButton"), CommonBundle.getCloseButtonText()}, (int)0, (Icon)Messages.getWarningIcon())) == 0) {
            BrowserUtil.open((String)StringUtil.defaultIfEmpty((String)url, (String)COPILOT_URL));
        }
    }

    public static boolean showTelemetryTermsDialog(Project project) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        return Messages.showYesNoDialog((Project)project, (String)CopilotBundle.get("github.login.telemetryConsent.message"), (String)CopilotBundle.get("github.login.telemetryConsent.title"), (String)CopilotBundle.get("github.login.telemetryConsent.yes"), (String)CopilotBundle.get("github.login.telemetryConsent.no"), (Icon)Messages.getWarningIcon()) == 0;
    }

    
}

