/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.diagnostic.AbstractMessage
 *  com.intellij.ide.DataManager
 *  com.intellij.openapi.actionSystem.CommonDataKeys
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.diagnostic.ErrorReportSubmitter
 *  com.intellij.openapi.diagnostic.IdeaLoggingEvent
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.diagnostic.SubmittedReportInfo
 *  com.intellij.openapi.diagnostic.SubmittedReportInfo$SubmissionStatus
 *  com.intellij.openapi.progress.ProgressIndicator
 *  com.intellij.openapi.progress.Task$Backgroundable
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.util.NlsActions$ActionText
 *  com.intellij.util.Consumer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.telemetry;

import com.github.copilot.CopilotBundle;
import com.github.copilot.telemetry.TelemetryService;
import com.intellij.diagnostic.AbstractMessage;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsActions;
import com.intellij.util.Consumer;
import java.awt.Component;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CopilotErrorHandler
extends ErrorReportSubmitter {
    private static final Logger LOG = Logger.getInstance(CopilotErrorHandler.class);

    @NlsActions.ActionText
        public String getReportActionText() {
        String string = CopilotBundle.get("errorHandler.reportAction.text");
        if (string == null) {
            CopilotErrorHandler.$$$reportNull$$$0(0);
        }
        return string;
    }

    public boolean submit(final IdeaLoggingEvent [] events, final String additionalInfo, Component parentComponent, final Consumer<? super SubmittedReportInfo> consumer) {
        if (parentComponent == null) {
            CopilotErrorHandler.$$$reportNull$$$0(1);
        }
        if (consumer == null) {
            CopilotErrorHandler.$$$reportNull$$$0(2);
        }
        if (events == null) {
            CopilotErrorHandler.$$$reportNull$$$0(3);
        }
        Project project = (Project)CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext(parentComponent));
        new Task.Backgroundable(project, CopilotBundle.get("errorHandler.reportDialog.title"), false){

            public void run(ProgressIndicator indicator) {
                if (indicator == null) {
                    1.$$$reportNull$$$0(0);
                }
                for (IdeaLoggingEvent event : events) {
                    Object data = event.getData();
                    if (data instanceof AbstractMessage) {
                        TelemetryService.getInstance().trackException(((AbstractMessage)data).getThrowable(), additionalInfo != null ? Map.of("userNotes", additionalInfo) : Map.of());
                        continue;
                    }
                    LOG.warn("Unable to report error report due to missing exception: " + event);
                }
                ApplicationManager.getApplication().invokeLater(() -> consumer.consume((Object)new SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.NEW_ISSUE)));
            }

            private static /* synthetic */ void $$$reportNull$$$0(int n) {
                throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", "indicator", "com/github/copilot/telemetry/CopilotErrorHandler$1", "run"));
            }
        }.queue();
        return true;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "method %s.%s must not return null";
                break;
            }
            case 1: 
            case 2: 
            case 3: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1: 
            case 2: 
            case 3: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/telemetry/CopilotErrorHandler";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "parentComponent";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "consumer";
                break;
            }
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "events";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getReportActionText";
                break;
            }
            case 1: 
            case 2: 
            case 3: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/telemetry/CopilotErrorHandler";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: 
            case 2: 
            case 3: {
                objectArray = objectArray;
                objectArray[2] = "submit";
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
            case 2: 
            case 3: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

