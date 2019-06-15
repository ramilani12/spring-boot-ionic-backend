package com.nelioalves.cursomc.services.exceptions;

public class DataIntegrityException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DataIntegrityException (final String msg) {
		super(msg);
	}
	
	public DataIntegrityException (final String msg, Throwable ex) {
		super(msg,ex);
	}

}
