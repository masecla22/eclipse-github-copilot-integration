package com.github.copilot.openai;

import java.util.Map;

public final class CompletionResponseInfo {
	private final String headerRequestId;
	private final String serverExperiments;
	private final String proxyRole;
	private final String modelEndpoint;
	private final long requestStartTimestamp;
	private final long processingTimeMillis;

	public Map<String, String> createTelemetryData() {
		return Map.of("headerRequestId", this.headerRequestId, "serverExperiments", this.serverExperiments, "proxyRole",
				this.proxyRole, "modelEndpoint", this.modelEndpoint);
	}

	public CompletionResponseInfo(String headerRequestId, String serverExperiments, String proxyRole,
			String modelEndpoint, long requestStartTimestamp, long processingTimeMillis) {
		this.headerRequestId = headerRequestId;
		this.serverExperiments = serverExperiments;
		this.proxyRole = proxyRole;
		this.modelEndpoint = modelEndpoint;
		this.requestStartTimestamp = requestStartTimestamp;
		this.processingTimeMillis = processingTimeMillis;
	}

	public String getHeaderRequestId() {
		return this.headerRequestId;
	}

	public String getServerExperiments() {
		return this.serverExperiments;
	}

	public String getProxyRole() {
		return this.proxyRole;
	}

	public String getModelEndpoint() {
		return this.modelEndpoint;
	}

	public long getRequestStartTimestamp() {
		return this.requestStartTimestamp;
	}

	public long getProcessingTimeMillis() {
		return this.processingTimeMillis;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof CompletionResponseInfo)) {
			return false;
		}
		CompletionResponseInfo other = (CompletionResponseInfo) o;
		if (this.getRequestStartTimestamp() != other.getRequestStartTimestamp()) {
			return false;
		}
		if (this.getProcessingTimeMillis() != other.getProcessingTimeMillis()) {
			return false;
		}
		String this$headerRequestId = this.getHeaderRequestId();
		String other$headerRequestId = other.getHeaderRequestId();
		if (this$headerRequestId == null ? other$headerRequestId != null
				: !this$headerRequestId.equals(other$headerRequestId)) {
			return false;
		}
		String this$serverExperiments = this.getServerExperiments();
		String other$serverExperiments = other.getServerExperiments();
		if (this$serverExperiments == null ? other$serverExperiments != null
				: !this$serverExperiments.equals(other$serverExperiments)) {
			return false;
		}
		String this$proxyRole = this.getProxyRole();
		String other$proxyRole = other.getProxyRole();
		if (this$proxyRole == null ? other$proxyRole != null : !this$proxyRole.equals(other$proxyRole)) {
			return false;
		}
		String this$modelEndpoint = this.getModelEndpoint();
		String other$modelEndpoint = other.getModelEndpoint();
		return !(this$modelEndpoint == null ? other$modelEndpoint != null
				: !this$modelEndpoint.equals(other$modelEndpoint));
	}

	public int hashCode() {
		int result = 1;
		long $requestStartTimestamp = this.getRequestStartTimestamp();
		result = result * 59 + (int) ($requestStartTimestamp >>> 32 ^ $requestStartTimestamp);
		long $processingTimeMillis = this.getProcessingTimeMillis();
		result = result * 59 + (int) ($processingTimeMillis >>> 32 ^ $processingTimeMillis);
		String $headerRequestId = this.getHeaderRequestId();
		result = result * 59 + ($headerRequestId == null ? 43 : $headerRequestId.hashCode());
		String $serverExperiments = this.getServerExperiments();
		result = result * 59 + ($serverExperiments == null ? 43 : $serverExperiments.hashCode());
		String $proxyRole = this.getProxyRole();
		result = result * 59 + ($proxyRole == null ? 43 : $proxyRole.hashCode());
		String $modelEndpoint = this.getModelEndpoint();
		result = result * 59 + ($modelEndpoint == null ? 43 : $modelEndpoint.hashCode());
		return result;
	}

	public String toString() {
		return "CompletionResponseInfo(headerRequestId=" + this.getHeaderRequestId() + ", serverExperiments="
				+ this.getServerExperiments() + ", proxyRole=" + this.getProxyRole() + ", modelEndpoint="
				+ this.getModelEndpoint() + ", requestStartTimestamp=" + this.getRequestStartTimestamp()
				+ ", processingTimeMillis=" + this.getProcessingTimeMillis() + ")";
	}
}
