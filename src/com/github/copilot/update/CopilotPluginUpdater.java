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
 *  com.intellij.openapi.progress.PerformInBackgroundOption
 *  com.intellij.openapi.progress.ProgressIndicator
 *  com.intellij.openapi.progress.ProgressManager
 *  com.intellij.openapi.progress.Task$Backgroundable
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.startup.StartupManager
 *  com.intellij.openapi.ui.MessageDialogBuilder
 *  com.intellij.openapi.updateSettings.impl.InternalPluginResults
 *  com.intellij.openapi.updateSettings.impl.PluginDownloader
 *  com.intellij.openapi.updateSettings.impl.PluginUpdates
 *  com.intellij.openapi.updateSettings.impl.UpdateChecker
 *  com.intellij.openapi.util.EmptyRunnable
 *  com.intellij.util.concurrency.annotations.RequiresBackgroundThread
 *  com.intellij.util.concurrency.annotations.RequiresEdt
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.update;

import com.github.copilot.CopilotBundle;
import com.github.copilot.CopilotNotifications;
import com.github.copilot.CopilotPlugin;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.updateSettings.impl.InternalPluginResults;
import com.intellij.openapi.updateSettings.impl.PluginDownloader;
import com.intellij.openapi.updateSettings.impl.PluginUpdates;
import com.intellij.openapi.updateSettings.impl.UpdateChecker;
import com.intellij.openapi.util.EmptyRunnable;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import com.intellij.util.concurrency.annotations.RequiresEdt;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public class CopilotPluginUpdater {
    private static final Logger LOG = Logger.getInstance(CopilotPluginUpdater.class);

    @RequiresEdt
    private static void notifyUpdateAvailable(Project project) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        Notification notification = CopilotNotifications.createFullContentNotification(CopilotBundle.get("pluginUpdate.updateNotification.title"), CopilotBundle.get("pluginUpdate.updateNotification.message"), NotificationType.IDE_UPDATE, true);
        notification.addAction((AnAction)NotificationAction.createSimpleExpiring((String)CopilotBundle.get("pluginUpdate.updateNotification.installButton"), () -> ApplicationManager.getApplication().executeOnPooledThread(() -> CopilotPluginUpdater.installCopilotUpdate(project))));
        notification.addAction((AnAction)NotificationAction.createSimpleExpiring((String)CopilotBundle.get("pluginUpdate.restartNotification.hideButton"), () -> {
            CopilotApplicationSettings.settings().checkForUpdate = false;
        }));
        notification.notify(project);
    }

    @RequiresEdt
    private static void notifyUpdateUnavailable(Project project) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        Notification notification = CopilotNotifications.createFullContentNotification(CopilotBundle.get("pluginUpdate.noUpdateNotification.title"), CopilotBundle.get("pluginUpdate.noUpdateNotification.message"), NotificationType.IDE_UPDATE, true);
        notification.addAction((AnAction)NotificationAction.createSimpleExpiring((String)CopilotBundle.get("pluginUpdate.noNotification.hideButton"), (Runnable)EmptyRunnable.INSTANCE));
        notification.notify(project);
    }

    @RequiresBackgroundThread
    private static void installCopilotUpdate(final Project project) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        new Task.Backgroundable(project, CopilotBundle.get("pluginUpdate.updatingTask.title"), true, PerformInBackgroundOption.ALWAYS_BACKGROUND){

            public void run(ProgressIndicator indicator) {
                if (indicator == null) {
                	throw new IllegalStateException("indicator cannot be null");
                }
                indicator.setIndeterminate(true);
                indicator.checkCanceled();
                InternalPluginResults availableUpdates = UpdateChecker.getInternalPluginUpdates();
                PluginUpdates updateDownloaders = availableUpdates.getPluginUpdates();
                PluginDownloader copilotUpdate = updateDownloaders.getAllEnabled().stream().filter(p -> CopilotPlugin.COPILOT_ID.equals((Object)p.getId())).findFirst().orElse(null);
                if (copilotUpdate == null) {
                    LOG.warn("Expected update of GitHub Copilot not found");
                    return;
                }
                indicator.checkCanceled();
                try {
                    if (copilotUpdate.prepareToInstallAndLoadDescriptor(indicator, false) == null) {
                        LOG.warn("Failed to download GitHub Copilot update file");
                        return;
                    }
                }
                catch (IOException e) {
                    LOG.warn("Failed to download GitHub Copilot update file", (Throwable)e);
                    return;
                }
                indicator.checkCanceled();
                ProgressManager.getInstance().executeNonCancelableSection(() -> {
                    try {
                        copilotUpdate.install();
                        ApplicationManager.getApplication().invokeLater(() -> CopilotPluginUpdater.notifyRequiredRestart(project));
                    }
                    catch (IOException e) {
                        LOG.warn("Failed to install update of GitHub Copilot", (Throwable)e);
                    }
                });
            }
        }.queue();
    }

    @RequiresEdt
    private static void notifyRequiredRestart(Project project) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if (!ApplicationManager.getApplication().isRestartCapable()) {
            return;
        }
        boolean ok = MessageDialogBuilder.yesNo((String)CopilotBundle.get("pluginUpdate.restartNotification.title"), (String)CopilotBundle.get("pluginUpdate.restartNotification.content")).ask(project);
        if (ok) {
            ApplicationManager.getApplication().restart();
        }
    }

    

    public static final class CheckUpdatesTask
    extends Task.Backgroundable {
                private final Project project;
        private final boolean notifyNoUpdate;

        public CheckUpdatesTask(Project project) {
            if (project == null) {
                throw new IllegalStateException("project cannot be null!");
            }
            this(project, false);
        }

        public CheckUpdatesTask(Project project, boolean notifyNoUpdate) {
            if (project == null) {
                throw new IllegalStateException("project cannot be null!");
            }
            super(project, CopilotBundle.get("pluginUpdate.checkUpdateTask.title"), true);
            this.project = project;
            this.notifyNoUpdate = notifyNoUpdate;
        }

        public void run(ProgressIndicator indicator) {
            if (indicator == null) {
                throw new IllegalStateException("indicator cannot be null!");
            }
            try {
                InternalPluginResults availableUpdates = UpdateChecker.getInternalPluginUpdates();
                boolean updateAvailable = availableUpdates.getPluginUpdates().getAllEnabled().stream().anyMatch(p -> CopilotPlugin.COPILOT_ID.equals((Object)p.getId()));
                if (updateAvailable) {
                    StartupManager.getInstance((Project)this.project).runWhenProjectIsInitialized(() -> CopilotPluginUpdater.notifyUpdateAvailable(this.project));
                } else if (this.notifyNoUpdate) {
                    StartupManager.getInstance((Project)this.project).runWhenProjectIsInitialized(() -> CopilotPluginUpdater.notifyUpdateUnavailable(this.project));
                }
            }
            catch (Exception e) {
                LOG.warn("Failed to check for plugin updates", (Throwable)e);
            }
        }   
    }
}

