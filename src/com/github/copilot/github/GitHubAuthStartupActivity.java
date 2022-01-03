/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.ide.BrowserUtil
 *  com.intellij.notification.Notification
 *  com.intellij.notification.NotificationAction
 *  com.intellij.notification.NotificationType
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.startup.StartupActivity$Background
 *  com.intellij.openapi.startup.StartupManager
 *  com.intellij.util.concurrency.annotations.RequiresBackgroundThread
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.github;

import com.github.copilot.CopilotBundle;
import com.github.copilot.CopilotNotifications;
import com.github.copilot.github.GitHubCopilotToken;
import com.github.copilot.github.GitHubService;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.github.copilot.status.CopilotStatus;
import com.github.copilot.status.CopilotStatusService;
import com.github.copilot.telemetry.TelemetryService;
import com.intellij.ide.BrowserUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jetbrains.annotations.NotNull;

public class GitHubAuthStartupActivity
implements StartupActivity.Background {
    private final AtomicBoolean hasRun = new AtomicBoolean(false);

    @RequiresBackgroundThread
    public void runActivity(Project project) {
        if (project == null) {
            GitHubAuthStartupActivity.$$$reportNull$$$0(0);
        }
        if (!this.hasRun.compareAndSet(false, true) || ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }
        GitHubService service = GitHubService.getInstance();
        service.refreshStatus();
        if (!service.isSignedIn()) {
            CopilotStatusService.notifyApplication(CopilotStatus.NotSignedIn);
            GitHubAuthStartupActivity.afterInit(project, () -> service.showLoginNotification(project, false));
            return;
        }
        GitHubCopilotToken copilotToken = service.fetchCopilotTokenInteractive(project, null, true, this::notifyUnauthorized, this::notifyTokenExpired);
        if (copilotToken != null && copilotToken.isValid() && !CopilotApplicationSettings.settings().isTelemetryTermsAccepted()) {
            CopilotStatusService.notifyApplication(CopilotStatus.TermsNotAccepted);
            this.promptTelemetryTerms(project);
        }
    }

    private void notifyUnauthorized(Project project, String url) {
        if (project == null) {
            GitHubAuthStartupActivity.$$$reportNull$$$0(1);
        }
        if (url == null) {
            GitHubAuthStartupActivity.$$$reportNull$$$0(2);
        }
        CopilotStatusService.notifyApplication(CopilotStatus.NotSignedIn);
        Notification notification = CopilotNotifications.createFullContentNotification(CopilotBundle.get("github.login.copilotUnauthorized.title"), CopilotBundle.get("github.login.copilotUnauthorized.message", url), NotificationType.WARNING, true);
        notification.addAction((AnAction)NotificationAction.createSimpleExpiring((String)CopilotBundle.get("github.login.copilotUnauthorized.joinButton"), () -> BrowserUtil.open((String)url)));
        GitHubAuthStartupActivity.afterInit(project, () -> notification.notify(project));
    }

    private void notifyTokenExpired(Project project) {
        if (project == null) {
            GitHubAuthStartupActivity.$$$reportNull$$$0(3);
        }
        CopilotStatusService.notifyApplication(CopilotStatus.UnknownError);
        Notification notification = CopilotNotifications.createFullContentNotification(CopilotBundle.get("github.login.copilotToken.unknownError.title"), CopilotBundle.get("github.login.copilotToken.unknownError.message"), NotificationType.ERROR, true);
        GitHubAuthStartupActivity.afterInit(project, () -> notification.notify(project));
    }

    private void promptTelemetryTerms(Project project) {
        if (project == null) {
            GitHubAuthStartupActivity.$$$reportNull$$$0(4);
        }
        Notification notification = CopilotNotifications.createFullContentNotification(CopilotBundle.get("github.login.telemetryConsent.title"), CopilotBundle.get("github.login.telemetryConsent.message"), NotificationType.WARNING, false);
        notification.addAction((AnAction)NotificationAction.createSimpleExpiring((String)CopilotBundle.get("github.login.telemetryConsent.yes"), () -> {
            CopilotApplicationSettings.settings().setTelemetryTermsAccepted(true);
            CopilotStatusService.notifyApplication(CopilotStatus.Ready);
            TelemetryService.getInstance().track("auth.telemetry_terms_accepted");
        }));
        notification.addAction((AnAction)NotificationAction.createSimpleExpiring((String)CopilotBundle.get("github.login.telemetryConsent.no"), () -> TelemetryService.getInstance().track("auth.telemetry_terms_rejected")));
        GitHubAuthStartupActivity.afterInit(project, () -> notification.notify(project));
    }

    private static void afterInit(Project project, Runnable action) {
        if (project == null) {
            GitHubAuthStartupActivity.$$$reportNull$$$0(5);
        }
        if (action == null) {
            GitHubAuthStartupActivity.$$$reportNull$$$0(6);
        }
        StartupManager.getInstance((Project)project).runWhenProjectIsInitialized(action);
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2;
        Object[] objectArray3 = new Object[3];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "project";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "url";
                break;
            }
            case 6: {
                objectArray2 = objectArray3;
                objectArray3[0] = "action";
                break;
            }
        }
        objectArray2[1] = "com/github/copilot/github/GitHubAuthStartupActivity";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "runActivity";
                break;
            }
            case 1: 
            case 2: {
                objectArray = objectArray2;
                objectArray2[2] = "notifyUnauthorized";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[2] = "notifyTokenExpired";
                break;
            }
            case 4: {
                objectArray = objectArray2;
                objectArray2[2] = "promptTelemetryTerms";
                break;
            }
            case 5: 
            case 6: {
                objectArray = objectArray2;
                objectArray2[2] = "afterInit";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

