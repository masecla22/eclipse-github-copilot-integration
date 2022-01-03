package com.github.copilot.lang.agent.commands;

import com.github.copilot.lang.agent.commands.SignInInitiateResult;

public class SignInInitiateSignedInResult
implements SignInInitiateResult {
    @Override
    public boolean isAlreadySignedIn() {
        return true;
    }
}

