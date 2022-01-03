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

public class AuthStatusResult {
    @SerializedName(value="status")
        public Status status;
    @SerializedName(value="status")
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

        public Status getStatus() {
        Status status = this.status;
        if (status == null) {
            throw new IllegalStateException("status cannot be null!");
        }
        return status;
    }

        public String getUser() {
        return this.user;
    }

    public void setStatus(Status status) {
        if (status == null) {
            throw new IllegalStateException("status cannot be null!");
        }
        this.status = status;
    }

    public void setUser(String user) {
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

    public AuthStatusResult(Status status, String user) {
        if (status == null) {
            throw new IllegalStateException("status cannot be null!");
        }
        this.status = status;
        this.user = user;
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

                static Status findById(String id) {
            if (id == null) {
                throw new IllegalStateException("id cannot be null!");
            }
            for (Status value : Status.values()) {
                if (!value.id.equals(id)) continue;
                return value;
            }
            return null;
        }
    }
}

