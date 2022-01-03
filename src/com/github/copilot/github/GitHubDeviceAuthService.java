/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonSyntaxException
 *  com.intellij.notification.NotificationType
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.progress.ProcessCanceledException
 *  com.intellij.openapi.progress.ProgressIndicator
 *  com.intellij.openapi.progress.ProgressManager
 *  com.intellij.openapi.progress.Task$WithResult
 *  com.intellij.openapi.progress.util.ProgressIndicatorUtils
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.ui.Messages
 *  com.intellij.util.Urls
 *  com.intellij.util.concurrency.AppExecutorUtil
 *  com.intellij.util.concurrency.annotations.RequiresBackgroundThread
 *  com.intellij.util.io.HttpRequests
 *  com.intellij.util.io.HttpRequests$HttpStatusException
 *  javax.annotation.concurrent.GuardedBy
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.TestOnly
 */
package com.github.copilot.github;

import com.github.copilot.CopilotBundle;
import com.github.copilot.CopilotNotifications;
import com.github.copilot.github.DeviceCodeResponse;
import com.github.copilot.github.DeviceLoginDialog;
import com.github.copilot.github.DeviceTokenResponse;
import com.github.copilot.github.GitHubAuthUtil;
import com.github.copilot.github.GitHubCopilotToken;
import com.github.copilot.github.GitHubService;
import com.github.copilot.github.GitHubSession;
import com.github.copilot.github.UnauthorizedTokenCallback;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.github.copilot.status.CopilotStatus;
import com.github.copilot.status.CopilotStatusService;
import com.github.copilot.telemetry.TelemetryService;
import com.github.copilot.util.ApplicationUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.util.ProgressIndicatorUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.Urls;
import com.intellij.util.concurrency.AppExecutorUtil;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import com.intellij.util.io.HttpRequests;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.annotation.concurrent.GuardedBy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

public class GitHubDeviceAuthService
implements GitHubService {
    private static final Logger LOG = Logger.getInstance(GitHubDeviceAuthService.class);
    private static final String GITHUB_APP_ID = "Iv1.b507a08c87ecfe98";
    private static final String CODE_REQUEST_URL = "https://github.com/login/device/code";
    private static final String DEVICE_POLL_URL = "https://github.com/login/oauth/access_token";
    private static final String GRANT_TYPE = "urn:ietf:params:oauth:grant-type:device_code";
    private static final Gson gson = new GsonBuilder().create();
    private final Object sessionLock = new Object();
    @GuardedBy(value="sessionLock")
    @Nullable
    private GitHubSession session;
    @GuardedBy(value="sessionLock")
    @Nullable
    private GitHubCopilotToken token;

    public GitHubDeviceAuthService() {
        this.refreshSession();
    }

    @Override
    public void refreshStatus() {
        this.refreshSession();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isSignedIn() {
        Object object = this.sessionLock;
        synchronized (object) {
            return this.session != null;
        }
    }

    @Override
    public void loginInteractive(@NotNull Project project) {
        DeviceCodeResponse codeResponse;
        if (project == null) {
            GitHubDeviceAuthService.$$$reportNull$$$0(0);
        }
        if ((codeResponse = this.retrieveDeviceCode(project)) == null) {
            CopilotNotifications.createFullContentNotification(CopilotBundle.get("deviceAuth.deviceCodeFetchFailed.title"), CopilotBundle.get("deviceAuth.deviceCodeFetchFailed.description"), NotificationType.ERROR, true);
            return;
        }
        if (!DeviceLoginDialog.showDeviceLogin(project, codeResponse)) {
            LOG.debug("Device login dialog was cancelled");
            return;
        }
        DeviceTokenResponse tokenResponse = this.retrieveToken(project, codeResponse);
        if (tokenResponse == null) {
            return;
        }
        if (tokenResponse.isError()) {
            TelemetryService.getInstance().track("auth.github_login_failed");
            String error = tokenResponse.isExpiredTokenError() ? CopilotBundle.get("github.login.errorExpiredMessage") : (tokenResponse.isIncorrectDeviceCodeError() ? CopilotBundle.get("github.login.errorIncorrectCodeMessage") : (tokenResponse.isAccessDeniedCodeError() ? CopilotBundle.get("github.login.errorAccessDeniedMessage") : CopilotBundle.get("github.login.errorGenericMessage")));
            Messages.showErrorDialog((Project)project, (String)error, (String)CopilotBundle.get("github.login.errorTitle"));
            return;
        }
        TelemetryService.getInstance().track("auth.new_login");
        GitHubSession newSession = new GitHubSession(tokenResponse.getAccessToken());
        GitHubCopilotToken newToken = this.fetchCopilotTokenInteractive(project, newSession, false, (ignoredProject, url) -> {
            GitHubAuthUtil.showUnauthorizedMessage(project, url);
            CopilotStatusService.notifyApplication(CopilotStatus.NotSignedIn);
        }, ignoredProject -> {
            Messages.showErrorDialog((Project)project, (String)CopilotBundle.get("github.login.copilotToken.unknownError.message"), (String)CopilotBundle.get("github.login.copilotToken.unknownError.title"));
            CopilotStatusService.notifyApplication(CopilotStatus.UnknownError);
        });
        if (!CopilotApplicationSettings.settings().isTelemetryTermsAccepted() && newToken != null) {
            boolean acceptedTerms = GitHubAuthUtil.showTelemetryTermsDialog(project);
            CopilotApplicationSettings.settings().setTelemetryTermsAccepted(acceptedTerms);
            TelemetryService.getInstance().track(acceptedTerms ? "auth.telemetry_terms_accepted" : "auth.telemetry_terms_rejected");
        }
        this.storeSessionData(newSession, newToken);
        if (newToken != null) {
            CopilotStatusService.notifyApplication(CopilotStatus.Ready);
            Messages.showInfoMessage((Project)project, (String)CopilotBundle.get("github.login.successMessage"), (String)CopilotBundle.get("github.login.successTitle"));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public GitHubCopilotToken fetchCopilotTokenInteractive(@NotNull Project project, @Nullable GitHubSession sessionOverride, boolean storeToken, @NotNull UnauthorizedTokenCallback onUnauthorized, @NotNull Consumer<Project> onNewTokenExpired) {
        GitHubCopilotToken newToken;
        GitHubSession session;
        if (project == null) {
            GitHubDeviceAuthService.$$$reportNull$$$0(1);
        }
        if (onUnauthorized == null) {
            GitHubDeviceAuthService.$$$reportNull$$$0(2);
        }
        if (onNewTokenExpired == null) {
            GitHubDeviceAuthService.$$$reportNull$$$0(3);
        }
        if ((session = sessionOverride) == null) {
            Object object = this.sessionLock;
            synchronized (object) {
                session = this.session;
            }
        }
        if (session == null) {
            LOG.debug("Missing session to fetch Copilot token");
            return null;
        }
        GitHubCopilotToken gitHubCopilotToken = newToken = ApplicationManager.getApplication().isDispatchThread() ? GitHubDeviceAuthService.retrieveCopilotTokenInBackground(project, session) : GitHubDeviceAuthService.retrieveCopilotToken(session);
        if (newToken == null) {
            LOG.debug("Failed to retrieve Copilot token for new session");
        } else if (newToken.isUnauthorized()) {
            newToken = null;
            onUnauthorized.onUnauthorizedToken(project, "https://copilot.github.com");
        } else if (newToken.isExpired()) {
            TelemetryService.getInstance().track("auth.invalid_expires_at", Map.of("expires_at", String.valueOf(newToken.getExpiresAtSeconds()), "now", String.valueOf(System.currentTimeMillis() / 1000L)));
            newToken = null;
            onNewTokenExpired.accept(project);
        }
        if (storeToken && newToken != null) {
            this.storeSessionData(null, newToken);
        }
        return newToken;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * WARNING - void declaration
     */
    @Override
    public GitHubCopilotToken getCopilotToken(boolean requestIfMissing, long minimumValidity, @NotNull TimeUnit timeUnit) {
        GitHubSession currentSession;
        if (timeUnit == null) {
            GitHubDeviceAuthService.$$$reportNull$$$0(4);
        }
        Object object = this.sessionLock;
        synchronized (object) {
            void timeUnit2;
            currentSession = this.session;
            if (currentSession != null && this.token != null && this.token.isValid() && !this.token.isExpiringSoon(minimumValidity, (TimeUnit)timeUnit2)) {
                return this.token;
            }
        }
        if (!requestIfMissing || currentSession == null) {
            return null;
        }
        ApplicationUtil.assertIsNonDispatchThread();
        GitHubCopilotToken newToken = GitHubDeviceAuthService.retrieveCopilotToken(currentSession);
        if (newToken != null) {
            if (newToken.isUnauthorized()) {
                LOG.warn("New Copilot token isn't authorized: " + newToken);
            } else if (newToken.isExpired()) {
                LOG.error("New Copilot token is expired: " + newToken);
                return null;
            }
            Object object2 = this.sessionLock;
            synchronized (object2) {
                this.token = newToken;
            }
            TelemetryService.getInstance().setTrackingId(newToken.getTrackingId());
        }
        return newToken;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void logout() {
        Object object = this.sessionLock;
        synchronized (object) {
            this.session = null;
            this.token = null;
        }
        CopilotApplicationSettings.setGitHubToken(null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @TestOnly
    public void setCopilotToken(@Nullable GitHubCopilotToken token) {
        Object object = this.sessionLock;
        synchronized (object) {
            this.token = token;
        }
    }

    @Nullable
    private DeviceCodeResponse retrieveDeviceCode(@NotNull Project project) {
        if (project == null) {
            GitHubDeviceAuthService.$$$reportNull$$$0(5);
        }
        try {
            return (DeviceCodeResponse)ProgressManager.getInstance().run((Task.WithResult)new Task.WithResult<DeviceCodeResponse, Exception>(project, "Retrieving GitHub Device Code", true){

                protected DeviceCodeResponse compute(@NotNull ProgressIndicator indicator) throws Exception {
                    if (indicator == null) {
                        1.$$$reportNull$$$0(0);
                    }
                    String url = Urls.newFromEncoded((String)GitHubDeviceAuthService.CODE_REQUEST_URL).addParameters(Map.of("client_id", GitHubDeviceAuthService.GITHUB_APP_ID, "scope", "read:user")).toExternalForm();
                    String json = HttpRequests.post((String)url, null).accept("application/json").readString(indicator);
                    return (DeviceCodeResponse)gson.fromJson(json, DeviceCodeResponse.class);
                }

                private static /* synthetic */ void $$$reportNull$$$0(int n) {
                    throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "indicator", "com/github/copilot/github/GitHubDeviceAuthService$1", "compute"));
                }
            });
        }
        catch (Exception e) {
            LOG.warn("error retrieving device code", (Throwable)e);
            return null;
        }
    }

    @Nullable
    private DeviceTokenResponse retrieveToken(@Nullable Project project, final @NotNull DeviceCodeResponse codeResponse) {
        if (codeResponse == null) {
            GitHubDeviceAuthService.$$$reportNull$$$0(6);
        }
        try {
            String title = CopilotBundle.get("deviceAuth.progressTitle");
            return (DeviceTokenResponse)ProgressManager.getInstance().run((Task.WithResult)new Task.WithResult<DeviceTokenResponse, Exception>(project, title, true){

                protected DeviceTokenResponse compute(@NotNull ProgressIndicator indicator) {
                    if (indicator == null) {
                        2.$$$reportNull$$$0(0);
                    }
                    indicator.setText2(CopilotBundle.get("deviceAuth.progressTitle2"));
                    long waitIntervalMillis = codeResponse.getIntervalMillis();
                    while (!indicator.isCanceled()) {
                        GitHubDeviceAuthService.this.waitForGitHub(indicator, waitIntervalMillis);
                        String url = Urls.newFromEncoded((String)GitHubDeviceAuthService.DEVICE_POLL_URL).addParameters(Map.of("client_id", GitHubDeviceAuthService.GITHUB_APP_ID, "device_code", codeResponse.getDeviceCode(), "grant_type", GitHubDeviceAuthService.GRANT_TYPE)).toExternalForm();
                        try {
                            String json = HttpRequests.post((String)url, null).accept("application/json").readString(indicator);
                            DeviceTokenResponse response = (DeviceTokenResponse)gson.fromJson(json, DeviceTokenResponse.class);
                            if (response.isAuthorizationPendingError()) continue;
                            if (response.isSlowDownError()) {
                                waitIntervalMillis = Math.max(response.getSlowDownIntervalMillis(), codeResponse.getIntervalMillis());
                                continue;
                            }
                            return response;
                        }
                        catch (ProcessCanceledException e) {
                            LOG.debug("Polling for device code cancelled by user", (Throwable)e);
                            return null;
                        }
                        catch (Exception e) {
                            LOG.debug("Exception waiting for GitHub token", (Throwable)e);
                        }
                    }
                    return null;
                }

                private static /* synthetic */ void $$$reportNull$$$0(int n) {
                    throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "indicator", "com/github/copilot/github/GitHubDeviceAuthService$2", "compute"));
                }
            });
        }
        catch (Exception e) {
            if (!(e instanceof ProcessCanceledException)) {
                LOG.warn("error polling for GitHub token", (Throwable)e);
            }
            return null;
        }
    }

    private void waitForGitHub(@NotNull ProgressIndicator indicator, long intervalMillis) {
        if (indicator == null) {
            GitHubDeviceAuthService.$$$reportNull$$$0(7);
        }
        ScheduledExecutorService service = AppExecutorUtil.getAppScheduledExecutorService();
        ScheduledFuture<?> future = service.schedule(() -> LOG.debug("Ready to poll GitHub device status"), intervalMillis, TimeUnit.MILLISECONDS);
        ProgressIndicatorUtils.awaitWithCheckCanceled(future, (ProgressIndicator)indicator);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void refreshSession() {
        String gitHubToken = CopilotApplicationSettings.getGitHubToken();
        if (gitHubToken != null) {
            Object object = this.sessionLock;
            synchronized (object) {
                this.session = new GitHubSession(gitHubToken);
                this.token = null;
            }
        }
        Object object = this.sessionLock;
        synchronized (object) {
            this.session = null;
            this.token = null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void storeSessionData(@Nullable GitHubSession newSession, @Nullable GitHubCopilotToken newCopilotToken) {
        Object object = this.sessionLock;
        synchronized (object) {
            if (newSession != null) {
                this.session = newSession;
            }
            if (newCopilotToken != null) {
                this.token = newCopilotToken;
            }
        }
        if (newSession != null) {
            CopilotApplicationSettings.setGitHubToken(newSession.getToken());
        }
        if (newCopilotToken != null) {
            TelemetryService.getInstance().setTrackingId(newCopilotToken.getTrackingId());
            TelemetryService.getInstance().track("auth.new_token");
        }
    }

    @Nullable
    private static GitHubCopilotToken retrieveCopilotTokenInBackground(@Nullable Project project, final @NotNull GitHubSession session) {
        if (session == null) {
            GitHubDeviceAuthService.$$$reportNull$$$0(8);
        }
        try {
            String title = CopilotBundle.get("github.auth.fetchingToken");
            Task.WithResult<GitHubCopilotToken, Exception> task = new Task.WithResult<GitHubCopilotToken, Exception>(project, title, true){

                protected GitHubCopilotToken compute(@NotNull ProgressIndicator indicator) {
                    if (indicator == null) {
                        3.$$$reportNull$$$0(0);
                    }
                    return GitHubDeviceAuthService.retrieveCopilotToken(session);
                }

                private static /* synthetic */ void $$$reportNull$$$0(int n) {
                    throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "indicator", "com/github/copilot/github/GitHubDeviceAuthService$3", "compute"));
                }
            };
            return (GitHubCopilotToken)ProgressManager.getInstance().run((Task.WithResult)task);
        }
        catch (ProcessCanceledException e) {
            return null;
        }
        catch (Exception e) {
            LOG.debug("Exception fetching GitHub proxy token", (Throwable)e);
            return null;
        }
    }

    @RequiresBackgroundThread
    @Nullable
    private static GitHubCopilotToken retrieveCopilotToken(@NotNull GitHubSession session) {
        if (session == null) {
            GitHubDeviceAuthService.$$$reportNull$$$0(9);
        }
        LOG.debug("Retrieving new GitHub Copilot proxy token");
        try {
            String json = HttpRequests.request((String)"https://api.github.com/copilot_internal/token").tuner(c -> c.addRequestProperty("Authorization", "token " + session.getToken())).readTimeout((int)TimeUnit.SECONDS.toMillis(5L)).readString(null);
            return (GitHubCopilotToken)gson.fromJson(json, GitHubCopilotToken.class);
        }
        catch (HttpRequests.HttpStatusException e) {
            TelemetryService.getInstance().track("auth.request_failed");
            LOG.debug("Unexpected HTTP status retrieving Copilot token", (Throwable)e);
            if (e.getStatusCode() == 403) {
                return GitHubCopilotToken.ACCESS_DENIED_TOKEN;
            }
        }
        catch (JsonSyntaxException e) {
            TelemetryService.getInstance().track("auth.request_read_failed");
            LOG.debug("Error retrieving Copilot token", (Throwable)e);
        }
        catch (Exception e) {
            TelemetryService.getInstance().track("auth.request_failed");
            LOG.debug("Error retrieving Copilot token", (Throwable)e);
        }
        return null;
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
                objectArray3[0] = "codeResponse";
                break;
            }
            case 7: {
                objectArray2 = objectArray3;
                objectArray3[0] = "indicator";
                break;
            }
            case 8: 
            case 9: {
                objectArray2 = objectArray3;
                objectArray3[0] = "session";
                break;
            }
        }
        objectArray2[1] = "com/github/copilot/github/GitHubDeviceAuthService";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "loginInteractive";
                break;
            }
            case 1: 
            case 2: 
            case 3: {
                objectArray = objectArray2;
                objectArray2[2] = "fetchCopilotTokenInteractive";
                break;
            }
            case 4: {
                objectArray = objectArray2;
                objectArray2[2] = "getCopilotToken";
                break;
            }
            case 5: {
                objectArray = objectArray2;
                objectArray2[2] = "retrieveDeviceCode";
                break;
            }
            case 6: {
                objectArray = objectArray2;
                objectArray2[2] = "retrieveToken";
                break;
            }
            case 7: {
                objectArray = objectArray2;
                objectArray2[2] = "waitForGitHub";
                break;
            }
            case 8: {
                objectArray = objectArray2;
                objectArray2[2] = "retrieveCopilotTokenInBackground";
                break;
            }
            case 9: {
                objectArray = objectArray2;
                objectArray2[2] = "retrieveCopilotToken";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objectArray));
    }
}

