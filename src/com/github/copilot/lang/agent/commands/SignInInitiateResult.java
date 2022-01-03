/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.commands;

import com.github.copilot.lang.agent.commands.SignInInitiateNotSignedInResult;
import com.github.copilot.lang.agent.commands.SignInInitiateSignedInResult;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.jetbrains.annotations.NotNull;

public interface SignInInitiateResult {
    public boolean isAlreadySignedIn();

    public static final class TypeAdapter
    implements JsonDeserializer<SignInInitiateResult> {
        public SignInInitiateResult deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            String status;
            JsonObject o = jsonElement.getAsJsonObject();
            switch (status = o.getAsJsonPrimitive("status").getAsString()) {
                case "PromptUserDeviceFlow": {
                    return this.readAuthRequired(o);
                }
                case "AlreadySignedIn": {
                    return new SignInInitiateSignedInResult();
                }
            }
            throw new IllegalStateException("Unexpected status: " + status);
        }

        private SignInInitiateResult readAuthRequired(@NotNull JsonObject o) {
            if (o == null) {
                TypeAdapter.$$$reportNull$$$0(0);
            }
            String userCode = o.getAsJsonPrimitive("userCode").getAsString();
            String uri = o.getAsJsonPrimitive("verificationUri").getAsString();
            long expiresIn = o.getAsJsonPrimitive("expiresIn").getAsLong();
            long interval = o.getAsJsonPrimitive("interval").getAsLong();
            return new SignInInitiateNotSignedInResult(userCode, uri, expiresIn, interval);
        }

        private static /* synthetic */ void $$$reportNull$$$0(int n) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "o", "com/github/copilot/lang/agent/commands/SignInInitiateResult$TypeAdapter", "readAuthRequired"));
        }
    }
}

