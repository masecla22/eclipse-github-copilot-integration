/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.diagnostic.Logger
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.vscodeRpc;

import com.github.copilot.lang.agent.rpc.JsonRPC;
import com.github.copilot.lang.agent.rpc.JsonRpcCommand;
import com.github.copilot.lang.agent.rpc.JsonRpcCommandSender;
import com.github.copilot.lang.agent.rpc.JsonRpcNotification;
import com.intellij.openapi.diagnostic.Logger;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

public class VSCodeJsonRpcCommandSender
implements JsonRpcCommandSender {
    private static final Logger LOG = Logger.getInstance(VSCodeJsonRpcCommandSender.class);
        private final OutputStream target;

    @Override
    public void sendCommand(int id, JsonRpcCommand<?> command) throws IOException {
        if (command == null) {
            throw new IllegalStateException("command cannot be null!");
        }
        String name = command.getCommandName();
        String commandJSON = JsonRPC.serializeCommand(id, name, command);
        if (LOG.isTraceEnabled()) {
            LOG.trace(String.format("[%d] sending command to agent: %s\n%s", id, name, commandJSON));
        } else if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("[%d] sending command to agent: %s", id, name));
        }
        this.sendBytes(commandJSON);
    }

    @Override
    public void sendNotification(JsonRpcNotification notification) throws IOException {
        if (notification == null) {
            throw new IllegalStateException("notification cannot be null!");
        }
        String name = notification.getCommandName();
        String commandJSON = JsonRPC.serializeNotification(name, notification);
        if (LOG.isTraceEnabled()) {
            LOG.trace(String.format("sending notification to agent: %s\n%s", name, commandJSON));
        } else if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("sending command to agent: %s", name));
        }
        this.sendBytes(commandJSON);
    }

    private void sendBytes(String commandJSON) throws IOException {
        byte[] commandBytes = commandJSON.getBytes(StandardCharsets.UTF_8);
        String message = "Content-Length: " + commandBytes.length + "\r\n\r\n";
        if (LOG.isTraceEnabled()) {
            LOG.trace("Sending JSON-RPC message:\n" + message + commandJSON);
        }
        this.target.write(message.getBytes(StandardCharsets.UTF_8));
        this.target.write(commandBytes);
        this.target.flush();
    }

    public VSCodeJsonRpcCommandSender(OutputStream target) {
        if (target == null) {
            throw new IllegalStateException("target cannot be null!");
        }
        if (target == null) {
            throw new NullPointerException("target is marked non-null but is null");
        }
        this.target = target;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2;
        Object[] objectArray3 = new Object[3];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "command";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "notification";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "target";
                break;
            }
        }
        objectArray2[1] = "com/github/copilot/lang/agent/vscodeRpc/VSCodeJsonRpcCommandSender";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "sendCommand";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[2] = "sendNotification";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[2] = "<init>";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

