package com.github.copilot.util;

public class RequestCancelledException
extends RuntimeException {
    public RequestCancelledException(String message) {
        super(message);
    }
}

