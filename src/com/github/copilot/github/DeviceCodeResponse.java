/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package com.github.copilot.github;

import com.google.gson.annotations.SerializedName;
import java.util.concurrent.TimeUnit;

public class DeviceCodeResponse {
    @SerializedName(value="device_code")
    private String deviceCode;
    @SerializedName(value="user_code")
    private String userCode;
    @SerializedName(value="verification_uri")
    private String verificationUri;
    @SerializedName(value="expires_in")
    private long expiresIn;
    @SerializedName(value="interval")
    private long intervalSeconds;

    public long getIntervalMillis() {
        return TimeUnit.SECONDS.toMillis(this.intervalSeconds);
    }

    public String getDeviceCode() {
        return this.deviceCode;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public String getVerificationUri() {
        return this.verificationUri;
    }

    public long getExpiresIn() {
        return this.expiresIn;
    }

    public long getIntervalSeconds() {
        return this.intervalSeconds;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public void setVerificationUri(String verificationUri) {
        this.verificationUri = verificationUri;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void setIntervalSeconds(long intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceCodeResponse)) {
            return false;
        }
        DeviceCodeResponse other = (DeviceCodeResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getExpiresIn() != other.getExpiresIn()) {
            return false;
        }
        if (this.getIntervalSeconds() != other.getIntervalSeconds()) {
            return false;
        }
        String this$deviceCode = this.getDeviceCode();
        String other$deviceCode = other.getDeviceCode();
        if (this$deviceCode == null ? other$deviceCode != null : !this$deviceCode.equals(other$deviceCode)) {
            return false;
        }
        String this$userCode = this.getUserCode();
        String other$userCode = other.getUserCode();
        if (this$userCode == null ? other$userCode != null : !this$userCode.equals(other$userCode)) {
            return false;
        }
        String this$verificationUri = this.getVerificationUri();
        String other$verificationUri = other.getVerificationUri();
        return !(this$verificationUri == null ? other$verificationUri != null : !this$verificationUri.equals(other$verificationUri));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeviceCodeResponse;
    }

    public int hashCode() {
        int result = 1;
        long $expiresIn = this.getExpiresIn();
        result = result * 59 + (int)($expiresIn >>> 32 ^ $expiresIn);
        long $intervalSeconds = this.getIntervalSeconds();
        result = result * 59 + (int)($intervalSeconds >>> 32 ^ $intervalSeconds);
        String $deviceCode = this.getDeviceCode();
        result = result * 59 + ($deviceCode == null ? 43 : $deviceCode.hashCode());
        String $userCode = this.getUserCode();
        result = result * 59 + ($userCode == null ? 43 : $userCode.hashCode());
        String $verificationUri = this.getVerificationUri();
        result = result * 59 + ($verificationUri == null ? 43 : $verificationUri.hashCode());
        return result;
    }

    public String toString() {
        return "DeviceCodeResponse(deviceCode=" + this.getDeviceCode() + ", userCode=" + this.getUserCode() + ", verificationUri=" + this.getVerificationUri() + ", expiresIn=" + this.getExpiresIn() + ", intervalSeconds=" + this.getIntervalSeconds() + ")";
    }

    public DeviceCodeResponse() {
    }

    public DeviceCodeResponse(String deviceCode, String userCode, String verificationUri, long expiresIn, long intervalSeconds) {
        this.deviceCode = deviceCode;
        this.userCode = userCode;
        this.verificationUri = verificationUri;
        this.expiresIn = expiresIn;
        this.intervalSeconds = intervalSeconds;
    }
}

