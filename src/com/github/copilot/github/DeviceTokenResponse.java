/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  com.intellij.openapi.util.text.StringUtil
 */
package com.github.copilot.github;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.util.text.StringUtil;
import java.util.concurrent.TimeUnit;

class DeviceTokenResponse {
	@SerializedName(value = "access_token")
	private String accessToken;
	@SerializedName(value = "token_type")
	private String tokenType;
	@SerializedName(value = "scope")
	private String scope;
	@SerializedName(value = "error")
	private String error;
	@SerializedName(value = "error_description")
	private String errorDescription;
	@SerializedName(value = "error_uri")
	private String errorURI;
	@SerializedName(value = "interval")
	private long slowDownIntervalSeconds;

	public boolean isSuccessful() {
		return !this.isError() && StringUtil.isNotEmpty((String) this.accessToken);
	}

	public boolean isError() {
		return StringUtil.isNotEmpty((String) this.error);
	}

	public boolean isAuthorizationPendingError() {
		return "authorization_pending".equals(this.error);
	}

	public boolean isSlowDownError() {
		return "slow_down".equals(this.error);
	}

	public boolean isExpiredTokenError() {
		return "expired_token".equals(this.error);
	}

	public boolean isIncorrectDeviceCodeError() {
		return "incorrect_device_code".equals(this.error);
	}

	public boolean isAccessDeniedCodeError() {
		return "access_denied".equals(this.error);
	}

	public long getSlowDownIntervalMillis() {
		return TimeUnit.SECONDS.toMillis(this.slowDownIntervalSeconds);
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public String getTokenType() {
		return this.tokenType;
	}

	public String getScope() {
		return this.scope;
	}

	public String getError() {
		return this.error;
	}

	public String getErrorDescription() {
		return this.errorDescription;
	}

	public String getErrorURI() {
		return this.errorURI;
	}

	public long getSlowDownIntervalSeconds() {
		return this.slowDownIntervalSeconds;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public void setErrorURI(String errorURI) {
		this.errorURI = errorURI;
	}

	public void setSlowDownIntervalSeconds(long slowDownIntervalSeconds) {
		this.slowDownIntervalSeconds = slowDownIntervalSeconds;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof DeviceTokenResponse)) {
			return false;
		}
		DeviceTokenResponse other = (DeviceTokenResponse) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (this.getSlowDownIntervalSeconds() != other.getSlowDownIntervalSeconds()) {
			return false;
		}
		String this$accessToken = this.getAccessToken();
		String other$accessToken = other.getAccessToken();
		if (this$accessToken == null ? other$accessToken != null : !this$accessToken.equals(other$accessToken)) {
			return false;
		}
		String this$tokenType = this.getTokenType();
		String other$tokenType = other.getTokenType();
		if (this$tokenType == null ? other$tokenType != null : !this$tokenType.equals(other$tokenType)) {
			return false;
		}
		String this$scope = this.getScope();
		String other$scope = other.getScope();
		if (this$scope == null ? other$scope != null : !this$scope.equals(other$scope)) {
			return false;
		}
		String this$error = this.getError();
		String other$error = other.getError();
		if (this$error == null ? other$error != null : !this$error.equals(other$error)) {
			return false;
		}
		String this$errorDescription = this.getErrorDescription();
		String other$errorDescription = other.getErrorDescription();
		if (this$errorDescription == null ? other$errorDescription != null
				: !this$errorDescription.equals(other$errorDescription)) {
			return false;
		}
		String this$errorURI = this.getErrorURI();
		String other$errorURI = other.getErrorURI();
		return !(this$errorURI == null ? other$errorURI != null : !this$errorURI.equals(other$errorURI));
	}

	protected boolean canEqual(Object other) {
		return other instanceof DeviceTokenResponse;
	}

	public int hashCode() {
		int result = 1;
		long $slowDownIntervalSeconds = this.getSlowDownIntervalSeconds();
		result = result * 59 + (int) ($slowDownIntervalSeconds >>> 32 ^ $slowDownIntervalSeconds);
		String $accessToken = this.getAccessToken();
		result = result * 59 + ($accessToken == null ? 43 : $accessToken.hashCode());
		String $tokenType = this.getTokenType();
		result = result * 59 + ($tokenType == null ? 43 : $tokenType.hashCode());
		String $scope = this.getScope();
		result = result * 59 + ($scope == null ? 43 : $scope.hashCode());
		String $error = this.getError();
		result = result * 59 + ($error == null ? 43 : $error.hashCode());
		String $errorDescription = this.getErrorDescription();
		result = result * 59 + ($errorDescription == null ? 43 : $errorDescription.hashCode());
		String $errorURI = this.getErrorURI();
		result = result * 59 + ($errorURI == null ? 43 : $errorURI.hashCode());
		return result;
	}

	public String toString() {
		return "DeviceTokenResponse(accessToken=" + this.getAccessToken() + ", tokenType=" + this.getTokenType()
				+ ", scope=" + this.getScope() + ", error=" + this.getError() + ", errorDescription="
				+ this.getErrorDescription() + ", errorURI=" + this.getErrorURI() + ", slowDownIntervalSeconds="
				+ this.getSlowDownIntervalSeconds() + ")";
	}
}
