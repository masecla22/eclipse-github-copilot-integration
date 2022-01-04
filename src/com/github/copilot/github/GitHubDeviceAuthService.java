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

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.annotation.concurrent.GuardedBy;

import org.eclipse.jface.dialogs.ProgressIndicator;
import org.eclipse.ui.internal.progress.ProgressManager;
import org.jetbrains.annotations.TestOnly;

import com.github.copilot.CopilotBundle;
import com.github.copilot.CopilotNotifications;
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
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.util.ProgressIndicatorUtils;
import com.intellij.openapi.project.Project;
import com.intellij.util.Urls;
import com.intellij.util.concurrency.AppExecutorUtil;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import com.intellij.util.io.HttpRequests;

public class GitHubDeviceAuthService implements GitHubService {
	private static final Logger LOG = Logger.getInstance(GitHubDeviceAuthService.class);
	private static final String GITHUB_APP_ID = "Iv1.b507a08c87ecfe98";
	private static final String CODE_REQUEST_URL = "https://github.com/login/device/code";
	private static final String DEVICE_POLL_URL = "https://github.com/login/oauth/access_token";
	private static final String GRANT_TYPE = "urn:ietf:params:oauth:grant-type:device_code";
	private static final Gson gson = new GsonBuilder().create();
	private final Object sessionLock = new Object();
	@GuardedBy(value = "sessionLock")
	private GitHubSession session;
	@GuardedBy(value = "sessionLock")
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
	public void loginInteractive(Project project) {
		DeviceCodeResponse codeResponse;
		if (project == null) {
			throw new IllegalStateException("project cannot be null!");
		}
		if ((codeResponse = this.retrieveDeviceCode(project)) == null) {
			CopilotNotifications.createFullContentNotification(
					CopilotBundle.get("deviceAuth.deviceCodeFetchFailed.title"),
					CopilotBundle.get("deviceAuth.deviceCodeFetchFailed.description"), NotificationType.ERROR, true);
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
			String error = tokenResponse.isExpiredTokenError() ? CopilotBundle.get("github.login.errorExpiredMessage")
					: (tokenResponse.isIncorrectDeviceCodeError()
							? CopilotBundle.get("github.login.errorIncorrectCodeMessage")
							: (tokenResponse.isAccessDeniedCodeError()
									? CopilotBundle.get("github.login.errorAccessDeniedMessage")
									: CopilotBundle.get("github.login.errorGenericMessage")));
			Messages.showErrorDialog((Project) project, (String) error,
					(String) CopilotBundle.get("github.login.errorTitle"));
			return;
		}
		TelemetryService.getInstance().track("auth.new_login");
		GitHubSession newSession = new GitHubSession(tokenResponse.getAccessToken());
		GitHubCopilotToken newToken = this.fetchCopilotTokenInteractive(project, newSession, false,
				(ignoredProject, url) -> {
					GitHubAuthUtil.showUnauthorizedMessage(project, url);
					CopilotStatusService.notifyApplication(CopilotStatus.NotSignedIn);
				}, ignoredProject -> {
					Messages.showErrorDialog((Project) project,
							(String) CopilotBundle.get("github.login.copilotToken.unknownError.message"),
							(String) CopilotBundle.get("github.login.copilotToken.unknownError.title"));
					CopilotStatusService.notifyApplication(CopilotStatus.UnknownError);
				});
		if (!CopilotApplicationSettings.settings().isTelemetryTermsAccepted() && newToken != null) {
			boolean acceptedTerms = GitHubAuthUtil.showTelemetryTermsDialog(project);
			CopilotApplicationSettings.settings().setTelemetryTermsAccepted(acceptedTerms);
			TelemetryService.getInstance()
					.track(acceptedTerms ? "auth.telemetry_terms_accepted" : "auth.telemetry_terms_rejected");
		}
		this.storeSessionData(newSession, newToken);
		if (newToken != null) {
			CopilotStatusService.notifyApplication(CopilotStatus.Ready);
			Messages.showInfoMessage((Project) project, (String) CopilotBundle.get("github.login.successMessage"),
					(String) CopilotBundle.get("github.login.successTitle"));
		}
	}

	/*
	 * WARNING - Removed try catching itself - possible behaviour change.
	 */
	@Override
	public GitHubCopilotToken fetchCopilotTokenInteractive(Project project, GitHubSession sessionOverride,
			boolean storeToken, UnauthorizedTokenCallback onUnauthorized, Consumer<Project> onNewTokenExpired) {
		GitHubCopilotToken newToken;
		GitHubSession session;
		if (project == null) {
			throw new IllegalStateException("project cannot be null!");
		}
		if (onUnauthorized == null) {
			throw new IllegalStateException("onUnauthorized cannot be null!");
		}
		if (onNewTokenExpired == null) {
			throw new IllegalStateException("onNewTokenExpired cannot be null!");
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
		GitHubCopilotToken gitHubCopilotToken = newToken = ApplicationManager.getApplication().isDispatchThread()
				? GitHubDeviceAuthService.retrieveCopilotTokenInBackground(project, session)
				: GitHubDeviceAuthService.retrieveCopilotToken(session);
		if (newToken == null) {
			LOG.debug("Failed to retrieve Copilot token for new session");
		} else if (newToken.isUnauthorized()) {
			newToken = null;
			onUnauthorized.onUnauthorizedToken(project, "https://copilot.github.com");
		} else if (newToken.isExpired()) {
			TelemetryService.getInstance().track("auth.invalid_expires_at",
					Map.of("expires_at", String.valueOf(newToken.getExpiresAtSeconds()), "now",
							String.valueOf(System.currentTimeMillis() / 1000L)));
			newToken = null;
			onNewTokenExpired.accept(project);
		}
		if (storeToken && newToken != null) {
			this.storeSessionData(null, newToken);
		}
		return newToken;
	}

	/*
	 * WARNING - Removed try catching itself - possible behaviour change. WARNING -
	 * void declaration
	 */
	@Override
	public GitHubCopilotToken getCopilotToken(boolean requestIfMissing, long minimumValidity, TimeUnit timeUnit) {
		GitHubSession currentSession;
		if (timeUnit == null) {
			throw new IllegalStateException("timeUnit cannot be null!");
		}
		Object object = this.sessionLock;
		synchronized (object) {
			void timeUnit2;
			currentSession = this.session;
			if (currentSession != null && this.token != null && this.token.isValid()
					&& !this.token.isExpiringSoon(minimumValidity, (TimeUnit) timeUnit2)) {
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
	public void setCopilotToken(GitHubCopilotToken token) {
		Object object = this.sessionLock;
		synchronized (object) {
			this.token = token;
		}
	}

	private DeviceCodeResponse retrieveDeviceCode(Project project) {
		if (project == null) {
			throw new IllegalStateException("project cannot be null!");
		}
		try {
			return (DeviceCodeResponse) ProgressManager.getInstance()
					.run((Task.WithResult) new Task.WithResult<DeviceCodeResponse, Exception>(project,
							"Retrieving GitHub Device Code", true) {

						protected DeviceCodeResponse compute(ProgressIndicator indicator) throws Exception {
							if (indicator == null) {
								throw new IllegalStateException("indicator cannot be null");
							}
							String url = Urls.newFromEncoded((String) GitHubDeviceAuthService.CODE_REQUEST_URL)
									.addParameters(Map.of("client_id", GitHubDeviceAuthService.GITHUB_APP_ID, "scope",
											"read:user"))
									.toExternalForm();
							String json = HttpRequests.post((String) url, null).accept("application/json")
									.readString(indicator);
							return (DeviceCodeResponse) gson.fromJson(json, DeviceCodeResponse.class);
						}
					});
		} catch (Exception e) {
			LOG.warn("error retrieving device code", (Throwable) e);
			return null;
		}
	}

	private DeviceTokenResponse retrieveToken(Project project, final DeviceCodeResponse codeResponse) {
		if (codeResponse == null) {
			throw new IllegalStateException("codeResponse cannot be null!");
		}
		try {
			String title = CopilotBundle.get("deviceAuth.progressTitle");
			return (DeviceTokenResponse) ProgressManager.getInstance()
					.run((Task.WithResult) new Task.WithResult<DeviceTokenResponse, Exception>(project, title, true) {

						protected DeviceTokenResponse compute(ProgressIndicator indicator) {
							if (indicator == null) {
								throw new IllegalStateException("indicator cannot be null");
							}
							indicator.setText2(CopilotBundle.get("deviceAuth.progressTitle2"));
							long waitIntervalMillis = codeResponse.getIntervalMillis();
							while (!indicator.isCanceled()) {
								GitHubDeviceAuthService.this.waitForGitHub(indicator, waitIntervalMillis);
								String url = Urls.newFromEncoded((String) GitHubDeviceAuthService.DEVICE_POLL_URL)
										.addParameters(Map.of("client_id", GitHubDeviceAuthService.GITHUB_APP_ID,
												"device_code", codeResponse.getDeviceCode(), "grant_type",
												GitHubDeviceAuthService.GRANT_TYPE))
										.toExternalForm();
								try {
									String json = HttpRequests.post((String) url, null).accept("application/json")
											.readString(indicator);
									DeviceTokenResponse response = (DeviceTokenResponse) gson.fromJson(json,
											DeviceTokenResponse.class);
									if (response.isAuthorizationPendingError())
										continue;
									if (response.isSlowDownError()) {
										waitIntervalMillis = Math.max(response.getSlowDownIntervalMillis(),
												codeResponse.getIntervalMillis());
										continue;
									}
									return response;
								} catch (ProcessCanceledException e) {
									LOG.debug("Polling for device code cancelled by user", (Throwable) e);
									return null;
								} catch (Exception e) {
									LOG.debug("Exception waiting for GitHub token", (Throwable) e);
								}
							}
							return null;
						}
					});
		} catch (Exception e) {
			if (!(e instanceof ProcessCanceledException)) {
				LOG.warn("error polling for GitHub token", (Throwable) e);
			}
			return null;
		}
	}

	private void waitForGitHub(ProgressIndicator indicator, long intervalMillis) {
		if (indicator == null) {
			throw new IllegalStateException("indicator cannot be null!");
		}
		ScheduledExecutorService service = AppExecutorUtil.getAppScheduledExecutorService();
		ScheduledFuture<?> future = service.schedule(() -> LOG.debug("Ready to poll GitHub device status"),
				intervalMillis, TimeUnit.MILLISECONDS);
		ProgressIndicatorUtils.awaitWithCheckCanceled(future, (ProgressIndicator) indicator);
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
	private void storeSessionData(GitHubSession newSession, GitHubCopilotToken newCopilotToken) {
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

	private static GitHubCopilotToken retrieveCopilotTokenInBackground(Project project, final GitHubSession session) {
		if (session == null) {
			throw new IllegalStateException("session cannot be null!");
		}
		try {
			String title = CopilotBundle.get("github.auth.fetchingToken");
			Task.WithResult<GitHubCopilotToken, Exception> task = new Task.WithResult<GitHubCopilotToken, Exception>(
					project, title, true) {

				protected GitHubCopilotToken compute(ProgressIndicator indicator) {
					if (indicator == null) {
						throw new IllegalStateException("indicator cannot be null");
					}
					return GitHubDeviceAuthService.retrieveCopilotToken(session);
				}
			};
			return (GitHubCopilotToken) ProgressManager.getInstance().run((Task.WithResult) task);
		} catch (ProcessCanceledException e) {
			return null;
		} catch (Exception e) {
			LOG.debug("Exception fetching GitHub proxy token", (Throwable) e);
			return null;
		}
	}

	@RequiresBackgroundThread
	private static GitHubCopilotToken retrieveCopilotToken(GitHubSession session) {
		if (session == null) {
			throw new IllegalStateException("session cannot be null!");
		}
		LOG.debug("Retrieving new GitHub Copilot proxy token");
		try {
			String json = HttpRequests.request((String) "https://api.github.com/copilot_internal/token")
					.tuner(c -> c.addRequestProperty("Authorization", "token " + session.getToken()))
					.readTimeout((int) TimeUnit.SECONDS.toMillis(5L)).readString(null);
			return gson.fromJson(json, GitHubCopilotToken.class);
		} catch (HttpRequests.HttpStatusException e) {
			TelemetryService.getInstance().track("auth.request_failed");
			LOG.debug("Unexpected HTTP status retrieving Copilot token", (Throwable) e);
			if (e.getStatusCode() == 403) {
				return GitHubCopilotToken.ACCESS_DENIED_TOKEN;
			}
		} catch (JsonSyntaxException e) {
			TelemetryService.getInstance().track("auth.request_read_failed");
			LOG.debug("Error retrieving Copilot token", (Throwable) e);
		} catch (Exception e) {
			TelemetryService.getInstance().track("auth.request_failed");
			LOG.debug("Error retrieving Copilot token", (Throwable) e);
		}
		return null;
	}

}
