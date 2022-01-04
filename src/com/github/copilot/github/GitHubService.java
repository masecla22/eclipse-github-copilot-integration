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

public interface GitHubService {
	public static GitHubService getInstance() {
		return (GitHubService) ApplicationManager.getApplication().getService(GitHubService.class);
	}

	@RequiresBackgroundThread
	public void refreshStatus();

	public boolean isSignedIn();

	@RequiresEdt
	public void loginInteractive(Project var1);

	public GitHubCopilotToken fetchCopilotTokenInteractive(Project var1, GitHubSession var2, boolean var3,
			UnauthorizedTokenCallback var4, Consumer<Project> var5);

	public void logout();

	public GitHubCopilotToken getCopilotToken(boolean var1, long var2, TimeUnit var4);

	default public GitHubCopilotToken getCopilotToken() {
		return this.getCopilotToken(false, 0L, TimeUnit.MILLISECONDS);
	}

	@RequiresEdt
	default public void showLoginNotification(Project project, boolean force) {
		if (project == null) {
			throw new IllegalStateException("project cannot be null!");
		}
		boolean shown = CopilotApplicationSettings.settings().signinNotificationShown;
		if (force || !shown) {
			if (!force) {
				CopilotApplicationSettings.settings().signinNotificationShown = true;
			}
			CopilotNotifications
					.createFullContentNotification(CopilotBundle.get("github.loginNotification.title"),
							CopilotBundle.get("github.loginNotification.text"), NotificationType.WARNING, true)
					.addAction((AnAction) NotificationAction.createExpiring(
							(String) CopilotBundle.get("github.loginNotification.loginAction"),
							(e, notification) -> GitHubService.getInstance().loginInteractive(project)))
					.notify(project);
		}
	}

}
