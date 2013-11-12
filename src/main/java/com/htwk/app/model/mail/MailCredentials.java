package com.htwk.app.model.mail;

import java.io.Serializable;

import com.htwk.app.model.impl.Credentials;

public class MailCredentials extends Credentials implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1934432139660328365L;

	private String protocol;
	private String host;
	private int port;

	public MailCredentials(String username, String password) {
		super(username, password);
	}

	public MailCredentials() {
		super();
	}

	/**
	 * @return the protocol
	 */
	public synchronized final String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol
	 *            the protocol to set
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
	 * @param host
	 *            the host to set
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
	 * @param port
	 *            the port to set
	 */
	public synchronized final void setPort(int port) {
		this.port = port;
	}

}
