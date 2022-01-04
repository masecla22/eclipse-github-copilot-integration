/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
 *  com.intellij.util.ExceptionUtil
 *  com.intellij.util.concurrency.annotations.RequiresBackgroundThread
 *  javax.annotation.concurrent.GuardedBy
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.concurrency.Promise
 */
package com.github.copilot.lang.agent;

import com.github.copilot.CopilotBundle;
import com.github.copilot.CopilotNotifications;
import com.github.copilot.github.DeviceCodeResponse;
import com.github.copilot.github.DeviceLoginDialog;
import com.github.copilot.github.GitHubAuthUtil;
import com.github.copilot.github.GitHubCopilotToken;
import com.github.copilot.github.GitHubService;
import com.github.copilot.github.GitHubSession;
import com.github.copilot.github.UnauthorizedTokenCallback;
import com.github.copilot.lang.agent.CopilotAgentProcessService;
import com.github.copilot.lang.agent.commands.AuthStatusResult;
import com.github.copilot.lang.agent.commands.CheckStatusCommand;
import com.github.copilot.lang.agent.commands.RecordTelemetryConsentCommand;
import com.github.copilot.lang.agent.commands.SignInConfirmCommand;
import com.github.copilot.lang.agent.commands.SignInInitiateCommand;
import com.github.copilot.lang.agent.commands.SignInInitiateNotSignedInResult;
import com.github.copilot.lang.agent.commands.SignInInitiateResult;
import com.github.copilot.lang.agent.commands.SignOutCommand;
import com.github.copilot.settings.CopilotApplicationSettings;
import com.github.copilot.status.CopilotStatus;
import com.github.copilot.status.CopilotStatusService;
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
import com.intellij.util.ExceptionUtil;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import javax.annotation.concurrent.GuardedBy;
import org.jetbrains.concurrency.Promise;

public class AgentGitHubService implements GitHubService {
	private static final Logger LOG = Logger.getInstance(AgentGitHubService.class);
	private static final int DEFAULT_TIMEOUT_MILLIS = (int) TimeUnit.SECONDS.toMillis(15L);
	private static final int SIGN_IN_TIMEOUT_MILLIS = (int) TimeUnit.SECONDS.toMillis(120L);
	private final Object sessionLock = new Object();
	@GuardedBy(value = "sessionLock")
	private AuthStatusResult status;

	/*
	 * WARNING - Removed try catching itself - possible behaviour change.
	 */
	@Override
	@RequiresBackgroundThread
	public void refreshStatus() {
		AuthStatusResult status = null;
		try {
			status = (AuthStatusResult) CopilotAgentProcessService.getInstance()
					.executeCommand(new CheckStatusCommand()).blockingGet(DEFAULT_TIMEOUT_MILLIS);
		} catch (ExecutionException | TimeoutException e) {
			LOG.warn((Throwable) e);
		}
		Object object = this.sessionLock;
		synchronized (object) {
			this.status = status;
		}
	}

	/*
	 * WARNING - Removed try catching itself - possible behaviour change.
	 */
	@Override
	public boolean isSignedIn() {
		Object object = this.sessionLock;
		synchronized (object) {
			return this.status != null && this.status.isSignedIn();
		}
	}

	@Override
	public void loginInteractive(Project project) {
		SignInInitiateResult initiateSignInResponse;
		if (project == null) {
			throw new IllegalStateException("project cannot be null!");
		}
		if ((initiateSignInResponse = this.initiateSignIn(project)) == null) {
			CopilotNotifications.createFullContentNotification(
					CopilotBundle.get("deviceAuth.deviceCodeFetchFailed.title"),
					CopilotBundle.get("deviceAuth.deviceCodeFetchFailed.description"), NotificationType.ERROR, true);
			return;
		}
		if (initiateSignInResponse.isAlreadySignedIn()) {
			LOG.debug("agent replied that the user is already signed in");
			CopilotStatusService.notifyApplication(CopilotStatus.Ready);
			return;
		}
		SignInInitiateNotSignedInResult notSignedInResult = (SignInInitiateNotSignedInResult) initiateSignInResponse;
		if (!DeviceLoginDialog.showDeviceLogin(project, this.asCodeResponse(notSignedInResult))) {
			LOG.debug("Device login dialog was cancelled");
			return;
		}
		AuthStatusResult confirmResult = this.confirmSignIn(project, notSignedInResult);
		if (confirmResult == null) {
			return;
		}
		if (confirmResult.isError()) {
			CopilotStatusService.notifyApplication(CopilotStatus.UnknownError);
			String error = CopilotBundle.get("github.login.errorGenericMessage");
			Messages.showErrorDialog((Project) project, (String) error,
					(String) CopilotBundle.get("github.login.errorTitle"));
			return;
		}
		if (confirmResult.isUnauthorized()) {
			CopilotStatusService.notifyApplication(CopilotStatus.NotSignedIn);
			GitHubAuthUtil.showUnauthorizedMessage(project, null);
			return;
		}
		if (confirmResult.isRequiringTelemetryConsent()) {
			boolean acceptedTerms = GitHubAuthUtil.showTelemetryTermsDialog(project);
			CopilotApplicationSettings.settings().setTelemetryTermsAccepted(acceptedTerms);
			AuthStatusResult status = this.recordTelemetryConsent(project);
			if (status != null && !status.isSignedIn()) {
				CopilotStatusService.notifyApplication(CopilotStatus.NotSignedIn);
				return;
			}
		}
		CopilotStatusService.notifyApplication(CopilotStatus.Ready);
		Messages.showInfoMessage((Project) project, (String) CopilotBundle.get("github.login.successMessage"),
				(String) CopilotBundle.get("github.login.successTitle"));
	}

	@Override
	public GitHubCopilotToken fetchCopilotTokenInteractive(Project project, GitHubSession sessionOverride,
			boolean storeToken, UnauthorizedTokenCallback onUnauthorized, Consumer<Project> onNewTokenExpired) {
		if (project == null) {
			throw new IllegalStateException("project cannot be null!");
		}
		if (onUnauthorized == null) {
			throw new IllegalStateException("onUnauthorized cannot be null!");
		}
		if (onNewTokenExpired == null) {
			throw new IllegalStateException("onNewTokenExpired cannot be null!");
		}
		return null;
	}

	/*
	 * WARNING - Removed try catching itself - possible behaviour change.
	 */
	@Override
	public void logout() {
		Object object = this.sessionLock;
		synchronized (object) {
			this.status = null;
		}
		CopilotAgentProcessService.getInstance().executeCommand(new SignOutCommand());
		ApplicationManager.getApplication().executeOnPooledThread(this::refreshStatus);
	}

	@Override
	public GitHubCopilotToken getCopilotToken(boolean requestIfMissing, long minimumValidity, TimeUnit timeUnit) {
		if (timeUnit == null) {
			throw new IllegalStateException("timeUnit cannot be null!");
		}
		return null;
	}

	private SignInInitiateResult initiateSignIn(Project project) {
		if (project == null) {
			throw new IllegalStateException("project cannot be null!");
		}
		try {
			return (SignInInitiateResult) ProgressManager.getInstance()
					.run((Task.WithResult) new Task.WithResult<SignInInitiateResult, Exception>(project,
							"Retrieving GitHub Device Code", true) {

						protected SignInInitiateResult compute(ProgressIndicator indicator) throws Exception {
							if (indicator == null) {
								throw new IllegalStateException("indicator cannot be null");
							}
							Promise<SignInInitiateResult> promise = CopilotAgentProcessService.getInstance()
									.executeCommand(new SignInInitiateCommand());
							return (SignInInitiateResult) promise.blockingGet(SIGN_IN_TIMEOUT_MILLIS);
						}
					});
		} catch (Exception e) {
			LOG.warn("error retrieving device code", (Throwable) e);
			return null;
		}
	}

	private DeviceCodeResponse asCodeResponse(SignInInitiateNotSignedInResult r) {
		if (r == null) {
			throw new IllegalStateException("r cannot be null!");
		}
		return new DeviceCodeResponse("", r.getUserCode(), r.getVerificationUri(), r.getExpiresInSeconds(),
				r.getIntervalSeconds());
	}

	private AuthStatusResult confirmSignIn(Project project, SignInInitiateNotSignedInResult code) {
		if (code == null) {
			throw new IllegalStateException("code cannot be null!");
		}
		try {
			String title = CopilotBundle.get("deviceAuth.progressTitle");
			return (AuthStatusResult) ProgressManager.getInstance()
					.run((Task.WithResult) new Task.WithResult<AuthStatusResult, Exception>(project, title, true) {

						protected AuthStatusResult compute(ProgressIndicator indicator) {
							if (indicator == null) {
								throw new IllegalStateException("indicator cannot be null");
							}
							indicator.setText2(CopilotBundle.get("deviceAuth.progressTitle2"));
							Promise<AuthStatusResult> responsePromise = CopilotAgentProcessService.getInstance()
									.executeCommand(new SignInConfirmCommand());
							return AgentGitHubService.awaitWithCheckCanceled(responsePromise, indicator);
						}
					});
		} catch (Exception e) {
			if (!(e instanceof ProcessCanceledException)) {
				LOG.warn("error polling for GitHub token", (Throwable) e);
			}
			return null;
		}
	}

	private AuthStatusResult recordTelemetryConsent(Project project) {
		if (project == null) {
			throw new IllegalStateException("project cannot be null!");
		}
		try {
			String title = CopilotBundle.get("deviceAuth.progressTitle");
			return (AuthStatusResult) ProgressManager.getInstance()
					.run((Task.WithResult) new Task.WithResult<AuthStatusResult, Exception>(project, title, true) {

						protected AuthStatusResult compute(ProgressIndicator indicator) {
							if (indicator == null) {
								throw new IllegalStateException("indicator cannot be null");
							}
							indicator.setText2(CopilotBundle.get("deviceAuth.progressTitle2"));
							Promise<AuthStatusResult> response = CopilotAgentProcessService.getInstance()
									.executeCommand(new RecordTelemetryConsentCommand());
							try {
								return (AuthStatusResult) response.blockingGet(DEFAULT_TIMEOUT_MILLIS);
							} catch (ExecutionException | TimeoutException e) {
								LOG.warn((Throwable) e);
								return null;
							}
						}
					});
		} catch (Exception e) {
			if (!(e instanceof ProcessCanceledException)) {
				LOG.warn("error polling for GitHub token", (Throwable) e);
			}
			return null;
		}
	}

	public static <T> T awaitWithCheckCanceled(Promise<T> promise, ProgressIndicator indicator) {
		if (promise == null) {
			throw new IllegalStateException("promise cannot be null!");
		}
		while (true) {
			ProgressIndicatorUtils.checkCancelledEvenWithPCEDisabled((ProgressIndicator) indicator);
			try {
				return (T) promise.blockingGet(10, TimeUnit.MILLISECONDS);
			} catch (RejectedExecutionException | TimeoutException exception) {
				continue;
			} catch (Throwable e) {
				Throwable cause = e.getCause();
				if (cause instanceof ProcessCanceledException) {
					throw (ProcessCanceledException) cause;
				}
				if (cause instanceof CancellationException) {
					throw new ProcessCanceledException(cause);
				}
				ExceptionUtil.rethrow((Throwable) e);
				continue;
			}
			break;
		}
	}

}
