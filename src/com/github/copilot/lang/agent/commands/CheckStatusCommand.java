/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.commands;

import com.github.copilot.lang.agent.commands.AuthStatusResult;
import com.github.copilot.lang.agent.rpc.JsonRpcCommand;
import com.google.gson.annotations.SerializedName;

public final class CheckStatusCommand
implements JsonRpcCommand<AuthStatusResult> {
    @SerializedName(value="localChecksOnly")
    private final boolean localChecksOnly = false;

    @Override
        public String getCommandName() {
        return "checkStatus";
    }

    @Override
        public Class<AuthStatusResult> getResponseType() {
        return AuthStatusResult.class;
    }

    public boolean isLocalChecksOnly() {
        return this.localChecksOnly;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckStatusCommand)) {
            return false;
        }
        CheckStatusCommand other = (CheckStatusCommand)o;
        return this.isLocalChecksOnly() == other.isLocalChecksOnly();
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + (this.isLocalChecksOnly() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "CheckStatusCommand(localChecksOnly=" + this.isLocalChecksOnly() + ")";
    }
}

