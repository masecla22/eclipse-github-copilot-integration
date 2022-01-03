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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public void loginInteractive(@NotNull Project project) {
        if (project == null) {
            DelegatingGitHubService.$$$reportNull$$$0(0);
        }
        this.getDelegate().loginInteractive(project);
    }

    @Override
    @Nullable
    public GitHubCopilotToken fetchCopilotTokenInteractive(@NotNull Project project, @Nullable GitHubSession sessionOverride, boolean storeToken, @NotNull UnauthorizedTokenCallback onUnauthorized, @NotNull Consumer<Project> onNewTokenExpired) {
        if (project == null) {
            DelegatingGitHubService.$$$reportNull$$$0(1);
        }
        if (onUnauthorized == null) {
            DelegatingGitHubService.$$$reportNull$$$0(2);
        }
        if (onNewTokenExpired == null) {
            DelegatingGitHubService.$$$reportNull$$$0(3);
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
    @Nullable
    public GitHubCopilotToken getCopilotToken(boolean requestIfMissing, long minimumValidity, @NotNull TimeUnit timeUnit) {
        void timeUnit2;
        if (timeUnit == null) {
            DelegatingGitHubService.$$$reportNull$$$0(4);
        }
        return this.getDelegate().getCopilotToken(requestIfMissing, minimumValidity, (TimeUnit)timeUnit2);
    }

    @Override
    @Nullable
    public GitHubCopilotToken getCopilotToken() {
        return this.getDelegate().getCopilotToken();
    }

    @Override
    @RequiresEdt
    public void showLoginNotification(@NotNull Project project, boolean force) {
        if (project == null) {
            DelegatingGitHubService.$$$reportNull$$$0(5);
        }
        this.getDelegate().showLoginNotification(project, force);
    }

    @NotNull
    private GitHubService getDelegate() {
        GitHubService gitHubService = CopilotAgent.isAgentSupportedAndEnabled() ? this.agentService : this.defaultService;
        if (gitHubService == null) {
            DelegatingGitHubService.$$$reportNull$$$0(6);
        }
        return gitHubService;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
            case 6: {
                string = "@NotNull method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 6: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "project";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "onUnauthorized";
                break;
            }
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "onNewTokenExpired";
                break;
            }
            case 4: {
                objectArray2 = objectArray3;
                objectArray3[0] = "timeUnit";
                break;
            }
            case 6: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/lang/agent/DelegatingGitHubService";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/lang/agent/DelegatingGitHubService";
                break;
            }
            case 6: {
                objectArray = objectArray2;
                objectArray2[1] = "getDelegate";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "loginInteractive";
                break;
            }
            case 1: 
            case 2: 
            case 3: {
                objectArray = objectArray;
                objectArray[2] = "fetchCopilotTokenInteractive";
                break;
            }
            case 4: {
                objectArray = objectArray;
                objectArray[2] = "getCopilotToken";
                break;
            }
            case 5: {
                objectArray = objectArray;
                objectArray[2] = "showLoginNotification";
                break;
            }
            case 6: {
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 6: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

