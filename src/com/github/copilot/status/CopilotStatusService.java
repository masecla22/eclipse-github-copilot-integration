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

    @NotNull
    public static CopilotStatus getCurrentStatus() {
        CopilotStatus copilotStatus = ((CopilotStatusService)ApplicationManager.getApplication().getService(CopilotStatusService.class)).status;
        if (copilotStatus == null) {
            CopilotStatusService.$$$reportNull$$$0(0);
        }
        return copilotStatus;
    }

    public CopilotStatusService() {
        ApplicationManager.getApplication().getMessageBus().connect((Disposable)this).subscribe(CopilotStatusListener.TOPIC, (Object)this);
    }

    public static void notifyApplication(@NotNull CopilotStatus status) {
        if (status == null) {
            CopilotStatusService.$$$reportNull$$$0(1);
        }
        ((CopilotStatusListener)ApplicationManager.getApplication().getMessageBus().syncPublisher(CopilotStatusListener.TOPIC)).onCopilotStatus(status);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onCopilotStatus(@NotNull CopilotStatus status) {
        if (status == null) {
            CopilotStatusService.$$$reportNull$$$0(2);
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

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "@NotNull method %s.%s must not return null";
                break;
            }
            case 1: 
            case 2: {
                string = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1: 
            case 2: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/status/CopilotStatusService";
                break;
            }
            case 1: 
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "status";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getCurrentStatus";
                break;
            }
            case 1: 
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/status/CopilotStatusService";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: {
                objectArray = objectArray;
                objectArray[2] = "notifyApplication";
                break;
            }
            case 2: {
                objectArray = objectArray;
                objectArray[2] = "onCopilotStatus";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
            case 1: 
            case 2: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

