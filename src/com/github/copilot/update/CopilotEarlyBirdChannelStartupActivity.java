/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.notification.Notification
 *  com.intellij.notification.NotificationAction
 *  com.intellij.notification.NotificationType
 *  com.intellij.openapi.actionSystem.AnAction
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.startup.StartupActivity
 *  com.intellij.openapi.updateSettings.impl.UpdateSettings
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.update;

import com.github.copilot.CopilotBundle;
import com.github.copilot.CopilotNotifications;
import com.github.copilot.settings.CopilotLocalApplicationSettings;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.updateSettings.impl.UpdateSettings;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CopilotEarlyBirdChannelStartupActivity implements StartupActivity {
	private static final Logger LOG = Logger.getInstance(CopilotEarlyBirdChannelStartupActivity.class);
	private static final String EARLY_BIRD_CHANNEL = "https://plugins.jetbrains.com/plugins/super-early-bird/list";
	private final AtomicBoolean hasRun = new AtomicBoolean(false);

	public void runActivity(Project project) {
		if (project == null) {
			throw new IllegalStateException("project cannot be null!");
		}
		if (!this.hasRun.compareAndSet(false, true) || ApplicationManager.getApplication().isUnitTestMode()) {
			return;
		}
		if (!CopilotLocalApplicationSettings.settings().checkEarlyBirdChannel) {
			return;
		}
		List hostListCopy = UpdateSettings.getInstance().getPluginHosts();
		if (!hostListCopy.contains(EARLY_BIRD_CHANNEL)) {
			return;
		}
		Notification notification = CopilotNotifications.createFullContentNotification(
				CopilotBundle.get("earlyBirdNotification.title"), CopilotBundle.get("earlyBirdNotification.message"),
				NotificationType.WARNING, true);
		notification.addAction((AnAction) NotificationAction
				.createSimpleExpiring((String) CopilotBundle.get("earlyBirdNotification.removeChannelButton"), () -> {
					CopilotLocalApplicationSettings.settings().checkEarlyBirdChannel = false;
					boolean ok = UpdateSettings.getInstance().getStoredPluginHosts()
							.removeIf(EARLY_BIRD_CHANNEL::equals);
					if (!ok) {
						LOG.warn("Failed to remove the early bird channel: " + hostListCopy);
					}
				}));
		notification.addAction((AnAction) NotificationAction
				.createSimpleExpiring((String) CopilotBundle.get("earlyBirdNotification.hideButton"), () -> {
					CopilotLocalApplicationSettings.settings().checkEarlyBirdChannel = false;
				}));
		notification.notify(project);
	}

}
