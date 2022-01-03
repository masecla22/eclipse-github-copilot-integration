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
import org.jetbrains.annotations.NotNull;

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
        if (uuids == null) {
            throw new NullPointerException("uuids is marked non-null but is null");
        }
        this.uuids = uuids;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "method %s.%s must not return null";
                break;
            }
            case 1: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/lang/agent/commands/NotifyRejectedCommand";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "uuids";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getUuids";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/lang/agent/commands/NotifyRejectedCommand";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: {
                objectArray = objectArray;
                objectArray[2] = "<init>";
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
            case 1: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

