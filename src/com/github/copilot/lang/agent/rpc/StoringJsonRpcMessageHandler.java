/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.compress.utils.Lists
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.rpc;

import com.github.copilot.lang.agent.rpc.JsonRpcMessageHandler;
import java.util.List;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

public class StoringJsonRpcMessageHandler
implements JsonRpcMessageHandler {
    private final List<String> messages = Lists.newArrayList();

    @Override
    public void handleJsonMessage(String message) {
        if (message == null) {
            StoringJsonRpcMessageHandler.$$$reportNull$$$0(0);
        }
        this.messages.add(message);
    }

    public List<String> getMessages() {
        return this.messages;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", "message", "com/github/copilot/lang/agent/rpc/StoringJsonRpcMessageHandler", "handleJsonMessage"));
    }
}

