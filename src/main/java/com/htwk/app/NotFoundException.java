package com.htwk.app;

import javax.servlet.http.HttpServletRequest;

public class NotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7936935796243299771L;

	public NotFoundException(HttpServletRequest req) {
		super();
	}
}
