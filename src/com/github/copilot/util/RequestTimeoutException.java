package com.github.copilot.util;

public class RequestTimeoutException
extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3700784861493313857L;

	public RequestTimeoutException(String message) {
        super(message);
    }
}

