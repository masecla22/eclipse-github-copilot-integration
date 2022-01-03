/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.commands;

import com.github.copilot.lang.agent.commands.SignInInitiateResult;
import com.github.copilot.lang.agent.rpc.JsonRpcCommand;
import org.jetbrains.annotations.NotNull;

public class SignInInitiateCommand
implements JsonRpcCommand<SignInInitiateResult> {
    @Override
        public String getCommandName() {
        return "signInInitiate";
    }

    @Override
        public Class<SignInInitiateResult> getResponseType() {
        return SignInInitiateResult.class;
    }
}

