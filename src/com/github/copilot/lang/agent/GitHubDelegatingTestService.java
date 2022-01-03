/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.application.ApplicationManager
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.TestOnly
 */
package com.github.copilot.lang.agent;

import com.github.copilot.github.GitHubCopilotToken;
import com.github.copilot.github.GitHubService;
import com.github.copilot.lang.agent.CopilotAgent;
import com.github.copilot.lang.agent.DelegatingGitHubService;
import com.intellij.openapi.application.ApplicationManager;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

@TestOnly
public class GitHubDelegatingTestService
extends DelegatingGitHubService {
    public static GitHubDelegatingTestService getInstance() {
        return (GitHubDelegatingTestService)ApplicationManager.getApplication().getService(GitHubService.class);
    }

    @TestOnly
    public void setCopilotTokenString(String token) {
        GitHubCopilotToken copilotToken = token == null ? null : new GitHubCopilotToken(token, System.currentTimeMillis() / 1000L + TimeUnit.HOURS.toSeconds(1L));
        this.setCopilotToken(copilotToken);
    }

    @TestOnly
    public void refreshGitHubSession() {
        if (CopilotAgent.isAgentSupportedAndEnabled()) {
            throw new UnsupportedOperationException("todo");
        }
        this.defaultService.refreshStatus();
    }

    @TestOnly
    public void setCopilotToken(GitHubCopilotToken token) {
        if (CopilotAgent.isAgentSupportedAndEnabled()) {
            throw new UnsupportedOperationException("todo");
        }
        this.defaultService.setCopilotToken(token);
    }
}

