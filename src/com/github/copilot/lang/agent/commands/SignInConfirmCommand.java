/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.commands;

import com.github.copilot.lang.agent.rpc.JsonRpcCommand;

public class SignInConfirmCommand implements JsonRpcCommand<AuthStatusResult> {
	@Override
	public String getCommandName() {
		return "signInConfirm";
	}

	@Override
	public Class<AuthStatusResult> getResponseType() {
		return AuthStatusResult.class;
	}
}
