/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.notification.NotificationAction
 *  com.intellij.notification.NotificationType
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.project.Project
 *  com.intellij.util.concurrency.annotations.RequiresBackgroundThread
 *  com.intellij.util.concurrency.annotations.RequiresEdt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.github;

import com.github.copilot.CopilotBundle;
import com.github.copilot.CopilotNotifications;
import com.github.copilot.github.GitHubCopilotToken;
import com.github.copilot.github.GitHubSession;
import com.github.copilot.github.UnauthorizedTokenCallback;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import com.intellij.util.concurrency.annotations.RequiresEdt;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GitHubService {
    public static GitHubService getInstance() {
        return (GitHubService)ApplicationManager.getApplication().getService(GitHubService.class);
    }

    @RequiresBackgroundThread
    public void refreshStatus();

    public boolean isSignedIn();

    @RequiresEdt
    public void loginInteractive(@NotNull Project var1);

    @Nullable
    public GitHubCopilotToken fetchCopilotTokenInteractive(@NotNull Project var1, @Nullable GitHubSession var2, boolean var3, @NotNull UnauthorizedTokenCallback var4, @NotNull Consumer<Project> var5);

    public void logout();

    @Nullable
    public GitHubCopilotToken getCopilotToken(boolean var1, long var2, @NotNull TimeUnit var4);

    @Nullable
    default public GitHubCopilotToken getCopilotToken() {
        return this.getCopilotToken(false, 0L, TimeUnit.MILLISECONDS);
    }

    @RequiresEdt
    default public void showLoginNotification(@NotNull Project project, boolean force) {
        if (project == null) {
            GitHubService.$$$reportNull$$$0(0);
        }
        boolean shown = CopilotApplicationSettings.settings().signinNotificationShown;
        if (force || !shown) {
            if (!force) {
                CopilotApplicationSettings.settings().signinNotificationShown = true;
            }
            CopilotNotifications.createFullContentNotification(CopilotBundle.get("github.loginNotification.title"), CopilotBundle.get("github.loginNotification.text"), NotificationType.WARNING, true).addAction((AnAction)NotificationAction.createExpiring((String)CopilotBundle.get("github.loginNotification.loginAction"), (e, notification) -> GitHubService.getInstance().loginInteractive(project))).notify(project);
        }
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "project", "com/github/copilot/github/GitHubService", "showLoginNotification"));
    }
}

