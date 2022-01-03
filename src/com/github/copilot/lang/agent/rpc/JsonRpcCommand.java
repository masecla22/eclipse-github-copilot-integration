/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.rpc;


public interface JsonRpcCommand<T> {
        public String getCommandName();

        public Class<T> getResponseType();
}

