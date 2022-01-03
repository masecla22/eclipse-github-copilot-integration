package com.github.copilot.util;

public class RequestTimeoutException
extends RuntimeException {
    public RequestTimeoutException(String message) {
        super(message);
    }
}

