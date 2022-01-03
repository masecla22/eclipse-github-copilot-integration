/*
 * Decompiled with CFR 0.152.
 */
package com.github.copilot.openai;

public class IncompatibleCopilotClientException
extends RuntimeException {
    private final int statusCode;

    public IncompatibleCopilotClientException(int statusCode) {
        super("plugin outdated");
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    @Override
    public String toString() {
        return "IncompatibleCopilotClientException(statusCode=" + this.getStatusCode() + ")";
    }
}

