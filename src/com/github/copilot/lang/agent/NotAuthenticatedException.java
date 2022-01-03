package com.github.copilot.lang.agent;

public class NotAuthenticatedException
extends RuntimeException {
    private final int requestId;

    public NotAuthenticatedException(int requestId) {
        super("Not signed in to Copilot agent");
        this.requestId = requestId;
    }

    public int getRequestId() {
        return this.requestId;
    }
}

