package com.st.novatech.springlms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "We do not have any copies of the requested book at this library")
public class NoCopiesException extends Exception {
	/**
	 * To throw an instance of this exception class, the caller must supply the
	 * exception message.
	 *
	 * @param errorMessage the exception message
	 */
	public NoCopiesException(final String errorMessage) {
		super(errorMessage);
	}
	/**
	 * To throw an instance of this exception class, the caller must supply the
	 * exception message and may supply a cause.
	 *
	 * @param errorMessage the exception message
	 * @param cause the exception that caused this one
	 */
	public NoCopiesException(final String errorMessage, final Throwable cause) {
		super(errorMessage, cause);
	}
}
