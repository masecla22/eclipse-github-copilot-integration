/*
 * Decompiled with CFR 0.152.
 */
package com.github.copilot.request;

import java.util.concurrent.atomic.AtomicInteger;

public final class RequestId {
    private static final AtomicInteger REQUEST_ID_SEQUENCE = new AtomicInteger();

    public static int currentRequestId() {
        return REQUEST_ID_SEQUENCE.get();
    }

    public static int incrementAndGet() {
        return REQUEST_ID_SEQUENCE.incrementAndGet();
    }
}

