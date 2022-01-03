/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonSyntaxException
 *  org.jetbrains.annotations.NotNull
 */
package com.github.copilot.lang.agent.rpc;

import com.github.copilot.lang.agent.commands.AuthStatusResult;
import com.github.copilot.lang.agent.commands.SignInInitiateResult;
import com.github.copilot.lang.agent.rpc.JsonRpcErrorException;
import com.github.copilot.lang.agent.rpc.JsonRpcResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

public final class JsonRPC {
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(AuthStatusResult.class, (Object)new AuthStatusResult.TypeAdapter()).registerTypeAdapter(SignInInitiateResult.class, (Object)new SignInInitiateResult.TypeAdapter()).create();

    private JsonRPC() {
    }

    public static String serializeCommand(int requestId, String name, Object command) {
        if (name == null) {
            throw new IllegalStateException("name cannot be null!");
        }
        if (command == null) {
            throw new IllegalStateException("command cannot be null!");
        }
        JsonObject json = new JsonObject();
        json.addProperty("jsonrpc", "2.0");
        json.addProperty("id", (Number)requestId);
        json.addProperty("method", name);
        json.add("params", GSON.toJsonTree(command));
        return GSON.toJson((JsonElement)json);
    }

    public static String serializeNotification(String name, Object command) {
        if (name == null) {
            throw new IllegalStateException("name cannot be null!");
        }
        if (command == null) {
            throw new IllegalStateException("command cannot be null!");
        }
        JsonObject json = new JsonObject();
        json.addProperty("jsonrpc", "2.0");
        json.addProperty("method", name);
        json.add("params", GSON.toJsonTree(command));
        return GSON.toJson((JsonElement)json);
    }

    public static JsonRpcResponse parseResponse(String responseContent) throws JsonRpcErrorException, JsonParseException {
        JsonObject response;
        if (responseContent == null) {
            throw new IllegalStateException("responseContent cannot be null!");
        }
        if ((response = (JsonObject)GSON.fromJson(responseContent, JsonObject.class)).has("error")) {
            int id = response.getAsJsonPrimitive("id").getAsInt();
            JsonObject error = (JsonObject)response.get("error");
            String message = error.getAsJsonPrimitive("message").getAsString();
            throw new JsonRpcErrorException(id, message);
        }
        int id = response.getAsJsonPrimitive("id").getAsInt();
        JsonElement resultJSON = response.get("result");
        return new JsonRpcResponse(id, resultJSON);
    }

        public static <T> T parseResponse(JsonElement json, Class<T> responseType) throws JsonSyntaxException {
        if (json == null) {
            throw new IllegalStateException("json cannot be null!");
        }
        Object object = GSON.fromJson(json, responseType);
        if (object == null) {
            throw new IllegalStateException("object cannot be null!");
        }
        return (T)object;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for parameter '%s' of %s.%s must not be null";
                break;
            }
            case 6: {
                string = "method %s.%s must not return null";
                break;
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 6: {
                n2 = 2;
                break;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "name";
                break;
            }
            case 1: 
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "command";
                break;
            }
            case 4: {
                objectArray2 = objectArray3;
                objectArray3[0] = "responseContent";
                break;
            }
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "json";
                break;
            }
            case 6: {
                objectArray2 = objectArray3;
                objectArray3[0] = "com/github/copilot/lang/agent/rpc/JsonRPC";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "com/github/copilot/lang/agent/rpc/JsonRPC";
                break;
            }
            case 6: {
                objectArray = objectArray2;
                objectArray2[1] = "parseResponse";
                break;
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray;
                objectArray[2] = "serializeCommand";
                break;
            }
            case 2: 
            case 3: {
                objectArray = objectArray;
                objectArray[2] = "serializeNotification";
                break;
            }
            case 4: 
            case 5: {
                objectArray = objectArray;
                objectArray[2] = "parseResponse";
                break;
            }
            case 6: {
                break;
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 6: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
        }
        throw runtimeException;
    }
}

