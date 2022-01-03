/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.lang.agent.commands;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AuthStatusResult {
    @SerializedName(value="status")
    @NotNull
    public Status status;
    @SerializedName(value="status")
    @Nullable
    public String user;

    public boolean isSignedIn() {
        return this.status == Status.Ok || this.status == Status.MaybeOk;
    }

    public boolean isRequiringTelemetryConsent() {
        return this.status == Status.NoTelemetryConsent;
    }

    public boolean isUnauthorized() {
        return this.status == Status.NotAuthorized;
    }

    public boolean isError() {
        return this.status == Status.FailedToGetToken || this.status == Status.TokenInvalid;
    }

    @NotNull
    public Status getStatus() {
        Status status = this.status;
        if (status == null) {
            AuthStatusResult.$$$reportNull$$$0(0);
        }
        return status;
    }

    @Nullable
    public String getUser() {
        return this.user;
    }

    public void setStatus(@NotNull Status status) {
        if (status == null) {
            AuthStatusResult.$$$reportNull$$$0(1);
        }
        if (status == null) {
            throw new NullPointerException("status is marked non-null but is null");
        }
        this.status = status;
    }

    public void setUser(@Nullable String user) {
        this.user = user;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AuthStatusResult)) {
            return false;
        }
        AuthStatusResult other = (AuthStatusResult)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Status this$status = this.getStatus();
        Status other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)((Object)this$status)).equals((Object)other$status)) {
            return false;
        }
        String this$user = this.getUser();
        String other$user = other.getUser();
        return !(this$user == null ? other$user != null : !this$user.equals(other$user));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AuthStatusResult;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Status $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)((Object)$status)).hashCode());
        String $user = this.getUser();
        result = result * 59 + ($user == null ? 43 : $user.hashCode());
        return result;
    }

    public String toString() {
        return "AuthStatusResult(status=" + this.getStatus() + ", user=" + this.getUser() + ")";
    }

    public AuthStatusResult(@NotNull Status status, @Nullable String user) {
        if (status == null) {
            AuthStatusResult.$$$reportNull$$$0(2);
        }
        if (status == null) {
            throw new NullPointerException("status is marked non-null but is null");
        }
        this.status = status;
        this.user = user;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "@NotNull method %s.%s must not return null";
                break;
            }
            case 1: 
            case 2: {
                string = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1: 
            case 2: {
                n2 = 3;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/lang/agent/commands/AuthStatusResult";
                break;
            }
            case 1: 
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "status";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getStatus";
                break;
            }
            case 1: 
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/lang/agent/commands/AuthStatusResult";
                break;
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 1: {
                objectArray = objectArray;
                objectArray[2] = "setStatus";
                break;
            }
            case 2: {
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
            case 1: 
            case 2: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
        }
        throw runtimeException;
    }

    public static final class TypeAdapter
    implements JsonDeserializer<AuthStatusResult> {
        public AuthStatusResult deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject o = jsonElement.getAsJsonObject();
            String statusId = o.getAsJsonPrimitive("status").getAsString();
            String user = o.has("user") ? o.getAsJsonPrimitive("user").getAsString() : null;
            Status status = Status.findById(statusId);
            if (status == null) {
                throw new JsonParseException("unknown status id value: " + statusId);
            }
            return new AuthStatusResult(status, user);
        }
    }

    static enum Status {
        Ok("OK"),
        MaybeOk("MaybeOK"),
        NotSignedIn("NotSignedIn"),
        NoTelemetryConsent("NoTelemetryConsent"),
        NotAuthorized("NotAuthorized"),
        FailedToGetToken("FailedToGetToken"),
        TokenInvalid("TokenInvalid");

        private final String id;

        private Status(String id) {
            this.id = id;
        }

        @Nullable
        static Status findById(@NotNull String id) {
            if (id == null) {
                Status.$$$reportNull$$$0(0);
            }
            for (Status value : Status.values()) {
                if (!value.id.equals(id)) continue;
                return value;
            }
            return null;
        }

        private static /* synthetic */ void $$$reportNull$$$0(int n) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "id", "com/github/copilot/lang/agent/commands/AuthStatusResult$Status", "findById"));
        }
    }
}

