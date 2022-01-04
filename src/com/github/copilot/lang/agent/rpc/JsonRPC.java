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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public final class JsonRPC {
	private static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(AuthStatusResult.class, new AuthStatusResult.TypeAdapter())
			.registerTypeAdapter(SignInInitiateResult.class, new SignInInitiateResult.TypeAdapter()).create();

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
		json.addProperty("id", requestId);
		json.addProperty("method", name);
		json.add("params", GSON.toJsonTree(command));
		return GSON.toJson(json);
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
		return GSON.toJson(json);
	}

	public static JsonRpcResponse parseResponse(String responseContent)
			throws JsonRpcErrorException, JsonParseException {
		JsonObject response;
		if (responseContent == null) {
			throw new IllegalStateException("responseContent cannot be null!");
		}
		if ((response = GSON.fromJson(responseContent, JsonObject.class)).has("error")) {
			int id = response.getAsJsonPrimitive("id").getAsInt();
			JsonObject error = (JsonObject) response.get("error");
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
		T object = GSON.fromJson(json, responseType);
		if (object == null) {
			throw new IllegalStateException("object cannot be null!");
		}
		return object;
	}

}
