/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  com.intellij.openapi.util.text.StringUtil
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.github;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.util.text.StringUtil;
import java.util.concurrent.TimeUnit;

public class GitHubCopilotToken {
    @SerializedName(value="token")
    private String token;
    @SerializedName(value="expires_at")
    private long expiresAtSeconds;
    public static final GitHubCopilotToken ACCESS_DENIED_TOKEN = new GitHubCopilotToken(null);

    GitHubCopilotToken(String token) {
        this.token = token;
        this.expiresAtSeconds = 0L;
    }

    public boolean isValid() {
        return !this.isExpired() && StringUtil.isNotEmpty((String)this.token);
    }

    public boolean isUnauthorized() {
        return StringUtil.isEmpty((String)this.token);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() / 1000L >= this.expiresAtSeconds;
    }

    public boolean isExpiringSoon(long timeDelta, TimeUnit timeUnit) {
        return timeUnit.toSeconds(timeDelta) + System.currentTimeMillis() / 1000L >= this.expiresAtSeconds;
    }

        public String getTrackingId() {
        String token = this.token;
        if (token == null) {
            return null;
        }
        int index = token.indexOf(58);
        return index == -1 ? null : token.substring(0, index);
    }

    public String getToken() {
        return this.token;
    }

    public long getExpiresAtSeconds() {
        return this.expiresAtSeconds;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiresAtSeconds(long expiresAtSeconds) {
        this.expiresAtSeconds = expiresAtSeconds;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GitHubCopilotToken)) {
            return false;
        }
        GitHubCopilotToken other = (GitHubCopilotToken)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getExpiresAtSeconds() != other.getExpiresAtSeconds()) {
            return false;
        }
        String this$token = this.getToken();
        String other$token = other.getToken();
        return !(this$token == null ? other$token != null : !this$token.equals(other$token));
    }

    protected boolean canEqual(Object other) {
        return other instanceof GitHubCopilotToken;
    }

    public int hashCode() {
        int result = 1;
        long $expiresAtSeconds = this.getExpiresAtSeconds();
        result = result * 59 + (int)($expiresAtSeconds >>> 32 ^ $expiresAtSeconds);
        String $token = this.getToken();
        result = result * 59 + ($token == null ? 43 : $token.hashCode());
        return result;
    }

    public String toString() {
        return "GitHubCopilotToken(token=" + this.getToken() + ", expiresAtSeconds=" + this.getExpiresAtSeconds() + ")";
    }

    public GitHubCopilotToken(String token, long expiresAtSeconds) {
        this.token = token;
        this.expiresAtSeconds = expiresAtSeconds;
    }
}

