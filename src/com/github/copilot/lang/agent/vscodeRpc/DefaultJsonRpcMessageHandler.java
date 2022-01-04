package com.github.copilot.lang.agent.vscodeRpc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.github.copilot.lang.agent.NotAuthenticatedException;
import com.github.copilot.lang.agent.rpc.JsonRPC;
import com.github.copilot.lang.agent.rpc.JsonRpcErrorException;
import com.github.copilot.lang.agent.rpc.JsonRpcMessageHandler;
import com.github.copilot.lang.agent.rpc.JsonRpcResponse;
import com.google.gson.JsonParseException;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.masecla.copilot.extra.Logger;

public class DefaultJsonRpcMessageHandler implements JsonRpcMessageHandler {
	private static final Logger LOG = Logger.getInstance(DefaultJsonRpcMessageHandler.class);
	@SuppressWarnings("rawtypes")
	private final ConcurrentMap<Integer, PendingRequest> pendingRequests = new ConcurrentHashMap<>();

	public <T> CompletableFuture<Object> addPendingRequest(int requestId, String commandName, Class<T> responseType) {
		CompletableFuture<Object> promise = new CompletableFuture<>();
		@SuppressWarnings("unchecked")
		PendingRequest<T> presentValue = this.pendingRequests.put(requestId,
				new PendingRequest<T>(promise, responseType, commandName, System.currentTimeMillis()));
		if (presentValue != null) {
			LOG.error("received non-unique request ID: " + requestId);
		}
		return promise;
	}

	@Override
	public void handleJsonMessage(String message) {
		JsonRpcResponse jsonResponse;
		if (message == null) {
			throw new IllegalStateException("message cannot be null!");
		}
		if (LOG.isTraceEnabled()) {
			LOG.trace(String.format("JSON message received: %s", message));
		}
		long timestamp = System.currentTimeMillis();
		try {
			jsonResponse = JsonRPC.parseResponse(message);
			LOG.debug("Parsed agent response JSON, resolving promise");
		} catch (JsonRpcErrorException e) {
			this.handleErrorResponse(e);
			return;
		} catch (JsonParseException e) {
			LOG.warn("JSON parsing error of agent response: " + message, e);
			return;
		}
		int id = jsonResponse.getRequestId();
		PendingRequest<?> pending = this.pendingRequests.remove(id);
		if (pending == null) {
			LOG.error("received unexpected response data for id: " + id);
			return;
		}
		try {
			LOG.debug(String.format("[%d] Response received. Command: %s, duration: %d ms", id, pending.commandName,
					timestamp - pending.startTimestamp));
			Object parsedResponse = JsonRPC.parseResponse(jsonResponse.getResponse(), pending.resultType);
			pending.getPromise().complete(parsedResponse);
		} catch (Exception e) {
			LOG.error("Error processing response of the agent", e);
			pending.promise.completeExceptionally(e);
		}
	}

	private void handleErrorResponse(JsonRpcErrorException e) {
		if (e == null) {
			throw new IllegalStateException("e cannot be null!");
		}
		int id = e.getRequestId();
		String message = e.getMessage();
		PendingRequest<?> pending = this.pendingRequests.remove(id);
		if (pending == null) {
			LOG.warn("no pending response found for request ID " + id);
			return;
		}
		if (message.contains("NotSignedIn")) {
			pending.promise.completeExceptionally(new NotAuthenticatedException(id));
		} else {
			pending.promise.completeExceptionally(e);
		}
	}

	@Getter
	@EqualsAndHashCode
	private static final class PendingRequest<T> {
		private final CompletableFuture<Object> promise;
		private final Class<T> resultType;
		private final String commandName;
		private final long startTimestamp;

		public PendingRequest(CompletableFuture<Object> promise, Class<T> resultType, String commandName,
				long startTimestamp) {
			this.promise = promise;
			this.resultType = resultType;
			this.commandName = commandName;
			this.startTimestamp = startTimestamp;
		}

		public String toString() {
			return "DefaultJsonRpcMessageHandler.PendingRequest(promise=" + this.getPromise() + ", resultType="
					+ this.getResultType() + ", commandName=" + this.getCommandName() + ", startTimestamp="
					+ this.getStartTimestamp() + ")";
		}
	}
}
