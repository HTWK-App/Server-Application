package com.htwk.app.model.impl;

import java.io.Serializable;

public class EncryptedCredentials implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5831197522596700728L;
	private String encryptedCredentials;
	private String salt;

	public EncryptedCredentials() {

	}

	public EncryptedCredentials(String encryptedCredentials, String salt) {
		this.encryptedCredentials = encryptedCredentials;
		this.salt = salt;
	}

	/**
	 * @return the encryptedCredentials
	 */
	public synchronized final String getEncryptedCredentials() {
		return encryptedCredentials;
	}

	/**
	 * @param encryptedCredentials the encryptedCredentials to set
	 */
	public synchronized final void setEncryptedCredentials(String encryptedCredentials) {
		this.encryptedCredentials = encryptedCredentials;
	}

	/**
	 * @return the salt
	 */
	public synchronized final String getSalt() {
		return salt;
	}

	/**
	 * @param salt the salt to set
	 */
	public synchronized final void setSalt(String salt) {
		this.salt = salt;
	}

}