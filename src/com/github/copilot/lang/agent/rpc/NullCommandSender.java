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
            NullCommandSender.$$$reportNull$$$0(0);
        }
    }

    @Override
    public void sendNotification(JsonRpcNotification notification) throws IOException {
        if (notification == null) {
            NullCommandSender.$$$reportNull$$$0(1);
        }
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
        }
        objectArray2[1] = "com/github/copilot/lang/agent/rpc/NullCommandSender";
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
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

