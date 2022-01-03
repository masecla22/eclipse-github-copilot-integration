/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonParseException
 *  com.intellij.openapi.application.ApplicationManager
 *  com.intellij.openapi.diagnostic.Logger
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.concurrency.AsyncPromise
 */
package com.github.copilot.lang.agent.vscodeRpc;

import com.github.copilot.lang.agent.NotAuthenticatedException;
import com.github.copilot.lang.agent.rpc.JsonRPC;
import com.github.copilot.lang.agent.rpc.JsonRpcErrorException;
import com.github.copilot.lang.agent.rpc.JsonRpcMessageHandler;
import com.github.copilot.lang.agent.rpc.JsonRpcResponse;
import com.google.gson.JsonParseException;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.concurrency.AsyncPromise;

public class DefaultJsonRpcMessageHandler
implements JsonRpcMessageHandler {
    private static final Logger LOG = Logger.getInstance(DefaultJsonRpcMessageHandler.class);
    private final ConcurrentMap<Integer, PendingRequest<?>> pendingRequests = new ConcurrentHashMap();

    public <T> AsyncPromise<T> addPendingRequest(int requestId, String commandName, Class<T> responseType) {
        AsyncPromise promise = new AsyncPromise();
        PendingRequest<T> presentValue = this.pendingRequests.put(requestId, new PendingRequest<T>(promise, responseType, commandName, System.currentTimeMillis()));
        if (presentValue != null) {
            LOG.error("received non-unique request ID: " + requestId);
        }
        return promise;
    }

    @Override
    public void handleJsonMessage(@NotNull String message) {
        JsonRpcResponse jsonResponse;
        if (message == null) {
            DefaultJsonRpcMessageHandler.$$$reportNull$$$0(0);
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace(String.format("JSON message received: %s", message));
        }
        long timestamp = System.currentTimeMillis();
        try {
            jsonResponse = JsonRPC.parseResponse(message);
            LOG.debug("Parsed agent response JSON, resolving promise");
        }
        catch (JsonRpcErrorException e) {
            this.handleErrorResponse(e);
            return;
        }
        catch (JsonParseException e) {
            LOG.warn("JSON parsing error of agent response: " + message, (Throwable)e);
            return;
        }
        int id = jsonResponse.getRequestId();
        PendingRequest pending = (PendingRequest)this.pendingRequests.remove(id);
        if (pending == null) {
            LOG.error("received unexpected response data for id: " + id);
            return;
        }
        try {
            LOG.debug(String.format("[%d] Response received. Command: %s, duration: %d ms", id, pending.commandName, timestamp - pending.startTimestamp));
            Object parsedResponse = JsonRPC.parseResponse(jsonResponse.getResponse(), pending.resultType);
            ApplicationManager.getApplication().executeOnPooledThread(() -> {
                AsyncPromise promise = pending.promise;
                promise.setResult(parsedResponse);
            });
        }
        catch (Exception e) {
            LOG.error("Error processing response of the agent", (Throwable)e);
            ApplicationManager.getApplication().executeOnPooledThread(() -> pending.promise.setError((Throwable)e));
        }
    }

    private void handleErrorResponse(@NotNull JsonRpcErrorException e) {
        if (e == null) {
            DefaultJsonRpcMessageHandler.$$$reportNull$$$0(1);
        }
        int id = e.getRequestId();
        String message = e.getMessage();
        PendingRequest pending = (PendingRequest)this.pendingRequests.remove(id);
        if (pending == null) {
            LOG.warn("no pending response found for request ID " + id);
            return;
        }
        if (message.contains("NotSignedIn")) {
            pending.promise.setError((Throwable)new NotAuthenticatedException(id));
        } else {
            pending.promise.setError((Throwable)e);
        }
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2;
        Object[] objectArray3 = new Object[3];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "message";
                break;
            }
            case 1: {
                objectArray2 = objectArray3;
                objectArray3[0] = "e";
                break;
            }
        }
        objectArray2[1] = "com/github/copilot/lang/agent/vscodeRpc/DefaultJsonRpcMessageHandler";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "handleJsonMessage";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[2] = "handleErrorResponse";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objectArray));
    }

    private static final class PendingRequest<T> {
        private final AsyncPromise<T> promise;
        private final Class<T> resultType;
        private final String commandName;
        private final long startTimestamp;

        public PendingRequest(AsyncPromise<T> promise, Class<T> resultType, String commandName, long startTimestamp) {
            this.promise = promise;
            this.resultType = resultType;
            this.commandName = commandName;
            this.startTimestamp = startTimestamp;
        }

        public AsyncPromise<T> getPromise() {
            return this.promise;
        }

        public Class<T> getResultType() {
            return this.resultType;
        }

        public String getCommandName() {
            return this.commandName;
        }

        public long getStartTimestamp() {
            return this.startTimestamp;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof PendingRequest)) {
                return false;
            }
            PendingRequest other = (PendingRequest)o;
            if (this.getStartTimestamp() != other.getStartTimestamp()) {
                return false;
            }
            AsyncPromise<T> this$promise = this.getPromise();
            AsyncPromise<T> other$promise = other.getPromise();
            if (this$promise == null ? other$promise != null : !this$promise.equals(other$promise)) {
                return false;
            }
            Class<T> this$resultType = this.getResultType();
            Class<T> other$resultType = other.getResultType();
            if (this$resultType == null ? other$resultType != null : !this$resultType.equals(other$resultType)) {
                return false;
            }
            String this$commandName = this.getCommandName();
            String other$commandName = other.getCommandName();
            return !(this$commandName == null ? other$commandName != null : !this$commandName.equals(other$commandName));
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            long $startTimestamp = this.getStartTimestamp();
            result = result * 59 + (int)($startTimestamp >>> 32 ^ $startTimestamp);
            AsyncPromise<T> $promise = this.getPromise();
            result = result * 59 + ($promise == null ? 43 : $promise.hashCode());
            Class<T> $resultType = this.getResultType();
            result = result * 59 + ($resultType == null ? 43 : $resultType.hashCode());
            String $commandName = this.getCommandName();
            result = result * 59 + ($commandName == null ? 43 : $commandName.hashCode());
            return result;
        }

        public String toString() {
            return "DefaultJsonRpcMessageHandler.PendingRequest(promise=" + this.getPromise() + ", resultType=" + this.getResultType() + ", commandName=" + this.getCommandName() + ", startTimestamp=" + this.getStartTimestamp() + ")";
        }
    }
}

