package com.nelioalves.cursomc.services.exceptions;

public class FileException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FileException (final String msg) {
		super(msg);
	}
	
	public FileException (final String msg, Throwable ex) {
		super(msg,ex);
	}

}
