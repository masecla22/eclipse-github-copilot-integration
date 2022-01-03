/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.project.Project
 *  com.intellij.util.concurrency.annotations.RequiresEdt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang.agent;

import com.github.copilot.github.GitHubCopilotToken;
import com.github.copilot.github.GitHubDeviceAuthService;
import com.github.copilot.github.GitHubService;
import com.github.copilot.github.GitHubSession;
import com.github.copilot.github.UnauthorizedTokenCallback;
import com.github.copilot.lang.agent.AgentGitHubService;
import com.github.copilot.lang.agent.CopilotAgent;
import com.intellij.openapi.project.Project;
import com.intellij.util.concurrency.annotations.RequiresEdt;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class DelegatingGitHubService
implements GitHubService {
    protected final AgentGitHubService agentService = new AgentGitHubService();
    protected final GitHubDeviceAuthService defaultService = new GitHubDeviceAuthService();

    @Override
    public void refreshStatus() {
        this.getDelegate().refreshStatus();
    }

    @Override
    public boolean isSignedIn() {
        return this.getDelegate().isSignedIn();
    }

    @Override
    @RequiresEdt
    public void loginInteractive(Project project) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        this.getDelegate().loginInteractive(project);
    }

    @Override
        public GitHubCopilotToken fetchCopilotTokenInteractive(Project project, GitHubSession sessionOverride, boolean storeToken, UnauthorizedTokenCallback onUnauthorized, Consumer<Project> onNewTokenExpired) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        if (onUnauthorized == null) {
            throw new IllegalStateException("onUnauthorized cannot be null!");
        }
        if (onNewTokenExpired == null) {
            throw new IllegalStateException("onNewTokenExpired cannot be null!");
        }
        return this.getDelegate().fetchCopilotTokenInteractive(project, sessionOverride, storeToken, onUnauthorized, onNewTokenExpired);
    }

    @Override
    public void logout() {
        this.getDelegate().logout();
    }

    /*
     * WARNING - void declaration
     */
    @Override
        public GitHubCopilotToken getCopilotToken(boolean requestIfMissing, long minimumValidity, TimeUnit timeUnit) {
        void timeUnit2;
        if (timeUnit == null) {
            throw new IllegalStateException("timeUnit cannot be null!");
        }
        return this.getDelegate().getCopilotToken(requestIfMissing, minimumValidity, (TimeUnit)timeUnit2);
    }

    @Override
        public GitHubCopilotToken getCopilotToken() {
        return this.getDelegate().getCopilotToken();
    }

    @Override
    @RequiresEdt
    public void showLoginNotification(Project project, boolean force) {
        if (project == null) {
            throw new IllegalStateException("project cannot be null!");
        }
        this.getDelegate().showLoginNotification(project, force);
    }

        private GitHubService getDelegate() {
        GitHubService gitHubService = CopilotAgent.isAgentSupportedAndEnabled() ? this.agentService : this.defaultService;
        if (gitHubService == null) {
            throw new IllegalStateException("gitHubService cannot be null!");
        }
        return gitHubService;
    }

    
}

