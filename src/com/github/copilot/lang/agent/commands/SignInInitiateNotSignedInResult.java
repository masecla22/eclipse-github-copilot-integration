/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.commands;

public final class SignInInitiateNotSignedInResult implements SignInInitiateResult {
	private final String userCode;
	private final String verificationUri;
	private final long expiresInSeconds;
	private final long intervalSeconds;

	@Override
	public boolean isAlreadySignedIn() {
		return false;
	}

	public SignInInitiateNotSignedInResult(String userCode, String verificationUri, long expiresInSeconds, long l) {
		if (userCode == null) {
			throw new IllegalStateException("userCode cannot be null!");
		}
		if (verificationUri == null) {
			throw new IllegalStateException("verificationUri cannot be null!");
		}
		this.userCode = userCode;
		this.verificationUri = verificationUri;
		this.expiresInSeconds = expiresInSeconds;
		this.intervalSeconds = 0l;
	}

	public String getUserCode() {
		String string = this.userCode;
		if (string == null) {
			throw new IllegalStateException("string cannot be null!");
		}
		return string;
	}

	public String getVerificationUri() {
		String string = this.verificationUri;
		if (string == null) {
			throw new IllegalStateException("string cannot be null!");
		}
		return string;
	}

	public long getExpiresInSeconds() {
		return this.expiresInSeconds;
	}

	public long getIntervalSeconds() {
		return this.intervalSeconds;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof SignInInitiateNotSignedInResult)) {
			return false;
		}
		SignInInitiateNotSignedInResult other = (SignInInitiateNotSignedInResult) o;
		if (this.getExpiresInSeconds() != other.getExpiresInSeconds()) {
			return false;
		}
		if (this.getIntervalSeconds() != other.getIntervalSeconds()) {
			return false;
		}
		String this$userCode = this.getUserCode();
		String other$userCode = other.getUserCode();
		if (this$userCode == null ? other$userCode != null : !this$userCode.equals(other$userCode)) {
			return false;
		}
		String this$verificationUri = this.getVerificationUri();
		String other$verificationUri = other.getVerificationUri();
		return !(this$verificationUri == null ? other$verificationUri != null
				: !this$verificationUri.equals(other$verificationUri));
	}

	public int hashCode() {
		int result = 1;
		long $expiresInSeconds = this.getExpiresInSeconds();
		result = result * 59 + (int) ($expiresInSeconds >>> 32 ^ $expiresInSeconds);
		long $intervalSeconds = this.getIntervalSeconds();
		result = result * 59 + (int) ($intervalSeconds >>> 32 ^ $intervalSeconds);
		String $userCode = this.getUserCode();
		result = result * 59 + ($userCode == null ? 43 : $userCode.hashCode());
		String $verificationUri = this.getVerificationUri();
		result = result * 59 + ($verificationUri == null ? 43 : $verificationUri.hashCode());
		return result;
	}

	public String toString() {
		return "SignInInitiateNotSignedInResult(userCode=" + this.getUserCode() + ", verificationUri="
				+ this.getVerificationUri() + ", expiresInSeconds=" + this.getExpiresInSeconds() + ", intervalSeconds="
				+ this.getIntervalSeconds() + ")";
	}

}
