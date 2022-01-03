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
    private static void notifyUpdateAvailable(@NotNull Project project) {
        if (project == null) {
            CopilotPluginUpdater.$$$reportNull$$$0(0);
        }
        Notification notification = CopilotNotifications.createFullContentNotification(CopilotBundle.get("pluginUpdate.updateNotification.title"), CopilotBundle.get("pluginUpdate.updateNotification.message"), NotificationType.IDE_UPDATE, true);
        notification.addAction((AnAction)NotificationAction.createSimpleExpiring((String)CopilotBundle.get("pluginUpdate.updateNotification.installButton"), () -> ApplicationManager.getApplication().executeOnPooledThread(() -> CopilotPluginUpdater.installCopilotUpdate(project))));
        notification.addAction((AnAction)NotificationAction.createSimpleExpiring((String)CopilotBundle.get("pluginUpdate.restartNotification.hideButton"), () -> {
            CopilotApplicationSettings.settings().checkForUpdate = false;
        }));
        notification.notify(project);
    }

    @RequiresEdt
    private static void notifyUpdateUnavailable(@NotNull Project project) {
        if (project == null) {
            CopilotPluginUpdater.$$$reportNull$$$0(1);
        }
        Notification notification = CopilotNotifications.createFullContentNotification(CopilotBundle.get("pluginUpdate.noUpdateNotification.title"), CopilotBundle.get("pluginUpdate.noUpdateNotification.message"), NotificationType.IDE_UPDATE, true);
        notification.addAction((AnAction)NotificationAction.createSimpleExpiring((String)CopilotBundle.get("pluginUpdate.noNotification.hideButton"), (Runnable)EmptyRunnable.INSTANCE));
        notification.notify(project);
    }

    @RequiresBackgroundThread
    private static void installCopilotUpdate(final @NotNull Project project) {
        if (project == null) {
            CopilotPluginUpdater.$$$reportNull$$$0(2);
        }
        new Task.Backgroundable(project, CopilotBundle.get("pluginUpdate.updatingTask.title"), true, PerformInBackgroundOption.ALWAYS_BACKGROUND){

            public void run(@NotNull ProgressIndicator indicator) {
                if (indicator == null) {
                    1.$$$reportNull$$$0(0);
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

            private static /* synthetic */ void $$$reportNull$$$0(int n) {
                throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "indicator", "com/github/copilot/update/CopilotPluginUpdater$1", "run"));
            }
        }.queue();
    }

    @RequiresEdt
    private static void notifyRequiredRestart(@NotNull Project project) {
        if (project == null) {
            CopilotPluginUpdater.$$$reportNull$$$0(3);
        }
        if (!ApplicationManager.getApplication().isRestartCapable()) {
            return;
        }
        boolean ok = MessageDialogBuilder.yesNo((String)CopilotBundle.get("pluginUpdate.restartNotification.title"), (String)CopilotBundle.get("pluginUpdate.restartNotification.content")).ask(project);
        if (ok) {
            ApplicationManager.getApplication().restart();
        }
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2 = new Object[3];
        objectArray2[0] = "project";
        objectArray2[1] = "com/github/copilot/update/CopilotPluginUpdater";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "notifyUpdateAvailable";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[2] = "notifyUpdateUnavailable";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[2] = "installCopilotUpdate";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[2] = "notifyRequiredRestart";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objectArray));
    }

    public static final class CheckUpdatesTask
    extends Task.Backgroundable {
        @NotNull
        private final Project project;
        private final boolean notifyNoUpdate;

        public CheckUpdatesTask(@NotNull Project project) {
            if (project == null) {
                CheckUpdatesTask.$$$reportNull$$$0(0);
            }
            this(project, false);
        }

        public CheckUpdatesTask(@NotNull Project project, boolean notifyNoUpdate) {
            if (project == null) {
                CheckUpdatesTask.$$$reportNull$$$0(1);
            }
            super(project, CopilotBundle.get("pluginUpdate.checkUpdateTask.title"), true);
            this.project = project;
            this.notifyNoUpdate = notifyNoUpdate;
        }

        public void run(@NotNull ProgressIndicator indicator) {
            if (indicator == null) {
                CheckUpdatesTask.$$$reportNull$$$0(2);
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
                    objectArray3[0] = "indicator";
                    break;
                }
            }
            objectArray2[1] = "com/github/copilot/update/CopilotPluginUpdater$CheckUpdatesTask";
            switch (n) {
                default: {
                    objectArray = objectArray2;
                    objectArray2[2] = "<init>";
                    break;
                }
                case 2: {
                    objectArray = objectArray2;
                    objectArray2[2] = "run";
                    break;
                }
            }
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objectArray));
        }
    }
}

