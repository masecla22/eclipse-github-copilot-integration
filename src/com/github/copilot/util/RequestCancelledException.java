package com.github.copilot.util;

public class RequestCancelledException
extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7197561304457046782L;

	public RequestCancelledException(String message) {
        super(message);
    }
}

