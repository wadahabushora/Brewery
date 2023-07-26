package com.brewery.management.exceptions;

public class CommonServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7381895799350615142L;

	public CommonServiceException() {
		super();
	}

	public CommonServiceException(String message) {
		super(message);
	}

	public CommonServiceException(Throwable cause) {
		super(cause);
	}

	public CommonServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
