/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.request;

import org.jetbrains.annotations.NotNull;

public enum BlockMode {
    Client,
    ServerSideIndentation;


    public boolean isAlwaysMultilineCompletion() {
        return this == ServerSideIndentation;
    }

    public boolean isSupportingOnDemandCompletions() {
        return this == ServerSideIndentation;
    }

    public int getMaxCompletions() {
        if (this == ServerSideIndentation) {
            return 1;
        }
        return 10;
    }

    @NotNull
    public String getTelemetryValue() {
        switch (this) {
            case Client: {
                return "parsing";
            }
            case ServerSideIndentation: {
                return "server";
            }
        }
        throw new IllegalStateException("Unexpected block mode: " + this);
    }
}

