/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.commands;

import com.github.copilot.lang.agent.commands.SignInInitiateResult;
import org.jetbrains.annotations.NotNull;

public final class SignInInitiateNotSignedInResult
implements SignInInitiateResult {
        private final String userCode;
        private final String verificationUri;
    private final long expiresInSeconds;
    private final long intervalSeconds;

    @Override
    public boolean isAlreadySignedIn() {
        return false;
    }

    /*
     * WARNING - void declaration
     */
    public SignInInitiateNotSignedInResult(String userCode, String verificationUri, long expiresInSeconds, long l) {
        void intervalSeconds;
        if (userCode == null) {
            throw new IllegalStateException("userCode cannot be null!");
        }
        if (verificationUri == null) {
            throw new IllegalStateException("verificationUri cannot be null!");
        }
        if (userCode == null) {
            throw new NullPointerException("userCode is marked non-null but is null");
        }
        if (verificationUri == null) {
            throw new NullPointerException("verificationUri is marked non-null but is null");
        }
        this.userCode = userCode;
        this.verificationUri = verificationUri;
        this.expiresInSeconds = expiresInSeconds;
        this.intervalSeconds = intervalSeconds;
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
        SignInInitiateNotSignedInResult other = (SignInInitiateNotSignedInResult)o;
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
        return !(this$verificationUri == null ? other$verificationUri != null : !this$verificationUri.equals(other$verificationUri));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $expiresInSeconds = this.getExpiresInSeconds();
        result = result * 59 + (int)($expiresInSeconds >>> 32 ^ $expiresInSeconds);
        long $intervalSeconds = this.getIntervalSeconds();
        result = result * 59 + (int)($intervalSeconds >>> 32 ^ $intervalSeconds);
        String $userCode = this.getUserCode();
        result = result * 59 + ($userCode == null ? 43 : $userCode.hashCode());
        String $verificationUri = this.getVerificationUri();
        result = result * 59 + ($verificationUri == null ? 43 : $verificationUri.hashCode());
        return result;
    }

    public String toString() {
        return "SignInInitiateNotSignedInResult(userCode=" + this.getUserCode() + ", verificationUri=" + this.getVerificationUri() + ", expiresInSeconds=" + this.getExpiresInSeconds() + ", intervalSeconds=" + this.getIntervalSeconds() + ")";
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
            case 2: 
            case 3: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 2: 
            case 3: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "userCode";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "verificationUri";
                break;
            }
            case 2: 
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/lang/agent/commands/SignInInitiateNotSignedInResult";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/lang/agent/commands/SignInInitiateNotSignedInResult";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "getUserCode";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[1] = "getVerificationUri";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "<init>";
                break;
            }
            case 2: 
            case 3: {
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 2: 
            case 3: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

