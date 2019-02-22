package com.nelioalves.cursomc.services.exceptions;

public class ObjectNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ObjectNotFoundException (final String msg) {
		super(msg);
	}
	
	public ObjectNotFoundException (final String msg, Throwable ex) {
		super(msg,ex);
	}

}
