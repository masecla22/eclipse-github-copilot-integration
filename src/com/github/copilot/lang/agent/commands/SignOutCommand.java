/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.commands;

import com.github.copilot.lang.agent.commands.AuthStatusResult;
import com.github.copilot.lang.agent.rpc.JsonRpcCommand;
import org.jetbrains.annotations.NotNull;

public class SignOutCommand
implements JsonRpcCommand<AuthStatusResult> {
    @Override
        public String getCommandName() {
        return "signOut";
    }

    @Override
        public Class<AuthStatusResult> getResponseType() {
        return AuthStatusResult.class;
    }
}

