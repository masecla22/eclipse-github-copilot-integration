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
import org.jetbrains.annotations.NotNull;

public final class NotifyShownCommand
implements JsonRpcCommand<String> {
    @SerializedName(value="uuid")
        private final String uuid;

    @Override
        public String getCommandName() {
        return "notifyShown";
    }

    @Override
        public Class<String> getResponseType() {
        return String.class;
    }

        public String getUuid() {
        String string = this.uuid;
        if (string == null) {
            NotifyShownCommand.$$$reportNull$$$0(0);
        }
        return string;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NotifyShownCommand)) {
            return false;
        }
        NotifyShownCommand other = (NotifyShownCommand)o;
        String this$uuid = this.getUuid();
        String other$uuid = other.getUuid();
        return !(this$uuid == null ? other$uuid != null : !this$uuid.equals(other$uuid));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $uuid = this.getUuid();
        result = result * 59 + ($uuid == null ? 43 : $uuid.hashCode());
        return result;
    }

    public String toString() {
        return "NotifyShownCommand(uuid=" + this.getUuid() + ")";
    }

    public NotifyShownCommand(String uuid) {
        if (uuid == null) {
            NotifyShownCommand.$$$reportNull$$$0(1);
        }
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        this.uuid = uuid;
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
                objectArray3[0] = "com/github/copilot/lang/agent/commands/NotifyShownCommand";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "uuid";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getUuid";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/lang/agent/commands/NotifyShownCommand";
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

