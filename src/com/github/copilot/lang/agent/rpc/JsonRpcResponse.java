/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 */
package com.github.copilot.lang.agent.rpc;

import com.google.gson.JsonElement;

public final class JsonRpcResponse {
    private final int requestId;
    private final JsonElement response;

    public JsonRpcResponse(int requestId, JsonElement response) {
        this.requestId = requestId;
        this.response = response;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public JsonElement getResponse() {
        return this.response;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof JsonRpcResponse)) {
            return false;
        }
        JsonRpcResponse other = (JsonRpcResponse)o;
        if (this.getRequestId() != other.getRequestId()) {
            return false;
        }
        JsonElement this$response = this.getResponse();
        JsonElement other$response = other.getResponse();
        return !(this$response == null ? other$response != null : !this$response.equals(other$response));
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + this.getRequestId();
        JsonElement $response = this.getResponse();
        result = result * 59 + ($response == null ? 43 : $response.hashCode());
        return result;
    }

    public String toString() {
        return "JsonRpcResponse(requestId=" + this.getRequestId() + ", response=" + this.getResponse() + ")";
    }
}

