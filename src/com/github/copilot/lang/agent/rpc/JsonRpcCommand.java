/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.rpc;

import org.jetbrains.annotations.NotNull;

public interface JsonRpcCommand<T> {
    @NotNull
    public String getCommandName();

    @NotNull
    public Class<T> getResponseType();
}

