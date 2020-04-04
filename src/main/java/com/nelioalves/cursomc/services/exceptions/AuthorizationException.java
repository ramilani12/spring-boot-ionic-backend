package com.nelioalves.cursomc.services.exceptions;

public class AuthorizationException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AuthorizationException (final String msg) {
		super(msg);
	}
	
	public AuthorizationException (final String msg, Throwable ex) {
		super(msg,ex);
	}

}
