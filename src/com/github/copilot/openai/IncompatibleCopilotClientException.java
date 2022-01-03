package com.github.copilot.openai;

public class IncompatibleCopilotClientException
extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2534703696185010188L;
	private final int statusCode;

    public IncompatibleCopilotClientException(int statusCode) {
        super("plugin outdated");
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    @Override
    public String toString() {
        return "IncompatibleCopilotClientException(statusCode=" + this.getStatusCode() + ")";
    }
}

