/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.commands;

import com.github.copilot.lang.agent.rpc.JsonRpcCommand;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public final class NotifyRejectedCommand
implements JsonRpcCommand<String> {
    @SerializedName(value="uuids")
        private final List<String> uuids;

    @Override
        public String getCommandName() {
        return "notifyRejected";
    }

    @Override
        public Class<String> getResponseType() {
        return String.class;
    }

        public List<String> getUuids() {
        List<String> list = this.uuids;
        if (list == null) {
            throw new IllegalStateException("list cannot be null!");
        }
        return list;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NotifyRejectedCommand)) {
            return false;
        }
        NotifyRejectedCommand other = (NotifyRejectedCommand)o;
        List<String> this$uuids = this.getUuids();
        List<String> other$uuids = other.getUuids();
        return !(this$uuids == null ? other$uuids != null : !((Object)this$uuids).equals(other$uuids));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<String> $uuids = this.getUuids();
        result = result * 59 + ($uuids == null ? 43 : ((Object)$uuids).hashCode());
        return result;
    }

    public String toString() {
        return "NotifyRejectedCommand(uuids=" + this.getUuids() + ")";
    }

    public NotifyRejectedCommand(List<String> uuids) {
        if (uuids == null) {
            throw new IllegalStateException("uuids cannot be null!");
        }
        this.uuids = uuids;
    }

    
}

