/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.concurrent.ThreadSafe
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.rpc;

import com.github.copilot.lang.agent.rpc.JsonRpcCommand;
import com.github.copilot.lang.agent.rpc.JsonRpcNotification;
import java.io.IOException;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface JsonRpcCommandSender {
    public void sendCommand(int var1, JsonRpcCommand<?> var2) throws IOException;

    public void sendNotification(JsonRpcNotification var1) throws IOException;
}

