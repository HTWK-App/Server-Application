package com.htwk.app.model.impl;

import java.io.Serializable;

public class Credentials implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5831197522596700728L;
	private String username;
	private String password;
	
	public Credentials(){
		
	}
	
	public Credentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	@Override
	public String toString() {
		return username + ":" + password;

	}

	/**
	 * @return the username
	 */
	public synchronized final String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public synchronized final void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public synchronized final String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public synchronized final void setPassword(String password) {
		this.password = password;
	}
}

