/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.rpc;

import com.github.copilot.lang.agent.rpc.JsonRpcCommand;
import com.github.copilot.lang.agent.rpc.JsonRpcCommandSender;
import com.github.copilot.lang.agent.rpc.JsonRpcNotification;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public class NullCommandSender
implements JsonRpcCommandSender {
    @Override
    public void sendCommand(int id, JsonRpcCommand<?> command) throws IOException {
        if (command == null) {
            throw new IllegalStateException("command cannot be null!");
        }
    }

    @Override
    public void sendNotification(JsonRpcNotification notification) throws IOException {
        if (notification == null) {
            throw new IllegalStateException("notification cannot be null!");
        }
    }

    
}

