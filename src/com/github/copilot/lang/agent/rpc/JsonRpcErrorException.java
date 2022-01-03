/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.rpc;

import org.jetbrains.annotations.NotNull;

public class JsonRpcErrorException
extends RuntimeException {
    private final int requestId;

    public JsonRpcErrorException(int requestId, String message) {
        if (message == null) {
            throw new IllegalStateException("message cannot be null!");
        }
        super(message);
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "JsonRpcErrorException(requestId=" + this.getRequestId() + ")";
    }

    public int getRequestId() {
        return this.requestId;
    }
}

