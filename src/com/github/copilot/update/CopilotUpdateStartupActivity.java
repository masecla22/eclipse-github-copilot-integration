/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  me.masecla.copilot.extra.Logger
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.startup.StartupActivity$Background
 *  com.intellij.util.concurrency.annotations.RequiresBackgroundThread
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.update;

import com.github.copilot.settings.CopilotApplicationSettings;
import com.github.copilot.settings.CopilotLocalApplicationSettings;
import com.github.copilot.update.CopilotPluginUpdater;
import com.intellij.openapi.application.ApplicationManager;
import me.masecla.copilot.extra.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class CopilotUpdateStartupActivity implements StartupActivity.Background {
	private static final Logger LOG = Logger.getInstance(CopilotUpdateStartupActivity.class);
	private final AtomicBoolean hasRun = new AtomicBoolean(false);

	@RequiresBackgroundThread
	public void runActivity(Project project) {
		if (project == null) {
			throw new IllegalStateException("project cannot be null!");
		}
		if (!this.hasRun.compareAndSet(false, true) || ApplicationManager.getApplication().isUnitTestMode()) {
			return;
		}
		if (!CopilotApplicationSettings.settings().checkForUpdate) {
			LOG.debug("Update check is disabled");
			return;
		}
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime lastCheck = CopilotLocalApplicationSettings.settings().lastUpdateCheck;
		if (lastCheck != null && lastCheck.plusDays(1L).isAfter(now)) {
			return;
		}
		CopilotLocalApplicationSettings.settings().lastUpdateCheck = now;
		new CopilotPluginUpdater.CheckUpdatesTask(project).queue();
	}

}
