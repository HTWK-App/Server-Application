package com.htwk.app.model.mail;

import java.io.Serializable;

public class MailCredentials implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1934432139660328365L;
	private String username;
	private String password;
	
	private String protocol;
	private String host;
	private int port;

	public MailCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public MailCredentials() {
	}

	/**
	 * @return the username
	 */
	public synchronized final String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
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
	 * @param password
	 *            the password to set
	 */
	public synchronized final void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return username + ":" + password;

	}

	/**
	 * @return the protocol
	 */
	public synchronized final String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public synchronized final void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the host
	 */
	public synchronized final String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public synchronized final void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public synchronized final int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public synchronized final void setPort(int port) {
		this.port = port;
	}

}
