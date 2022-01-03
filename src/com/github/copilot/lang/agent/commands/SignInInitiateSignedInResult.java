package com.github.copilot.lang.agent.commands;

public class SignInInitiateSignedInResult
implements SignInInitiateResult {
    @Override
    public boolean isAlreadySignedIn() {
        return true;
    }
}

