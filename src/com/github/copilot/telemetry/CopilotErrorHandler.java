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
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

    public boolean submit(final IdeaLoggingEvent [] events, final String additionalInfo, Component parentComponent, final Consumer<? super SubmittedReportInfo> consumer) {
        if (parentComponent == null) {
            throw new IllegalStateException("parentComponent cannot be null!");
        }
        if (consumer == null) {
            throw new IllegalStateException("consumer cannot be null!");
        }
        if (events == null) {
            throw new IllegalStateException("events cannot be null!");
        }
        Project project = (Project)CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext(parentComponent));
        new Task.Backgroundable(project, CopilotBundle.get("errorHandler.reportDialog.title"), false){

            public void run(ProgressIndicator indicator) {
                if (indicator == null) {
                	throw new IllegalStateException("indicator cannot be null");
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
        }.queue();
        return true;
    }

    
}

