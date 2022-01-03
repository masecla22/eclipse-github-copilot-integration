/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.application.Application
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.project.ProjectManager
 *  javax.annotation.concurrent.GuardedBy
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.status;

import com.github.copilot.status.CopilotStatus;
import com.github.copilot.status.CopilotStatusListener;
import com.github.copilot.statusBar.CopilotStatusBarWidget;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import javax.annotation.concurrent.GuardedBy;
import org.jetbrains.annotations.NotNull;

public class CopilotStatusService
implements CopilotStatusListener,
Disposable {
    private final Object lock = new Object();
    @GuardedBy(value="lock")
    private CopilotStatus status = CopilotStatus.Ready;

        public static CopilotStatus getCurrentStatus() {
        CopilotStatus copilotStatus = ((CopilotStatusService)ApplicationManager.getApplication().getService(CopilotStatusService.class)).status;
        if (copilotStatus == null) {
            throw new IllegalStateException("copilotStatus cannot be null!");
        }
        return copilotStatus;
    }

    public CopilotStatusService() {
        ApplicationManager.getApplication().getMessageBus().connect((Disposable)this).subscribe(CopilotStatusListener.TOPIC, (Object)this);
    }

    public static void notifyApplication(CopilotStatus status) {
        if (status == null) {
            throw new IllegalStateException("status cannot be null!");
        }
        ((CopilotStatusListener)ApplicationManager.getApplication().getMessageBus().syncPublisher(CopilotStatusListener.TOPIC)).onCopilotStatus(status);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onCopilotStatus(CopilotStatus status) {
        if (status == null) {
            throw new IllegalStateException("status cannot be null!");
        }
        boolean notify = false;
        Object object = this.lock;
        synchronized (object) {
            notify = this.status != status;
            this.status = status;
        }
        if (notify) {
            Runnable action = () -> {
                for (Project project : ProjectManager.getInstance().getOpenProjects()) {
                    CopilotStatusBarWidget.update(project);
                }
            };
            Application application = ApplicationManager.getApplication();
            if (application.isDispatchThread()) {
                action.run();
            } else {
                application.invokeLater(action);
            }
        }
    }

    public void dispose() {
    }

    
}

