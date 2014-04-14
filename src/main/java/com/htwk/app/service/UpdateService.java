package com.htwk.app.service;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.CredentialException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.stereotype.Service;

import com.htwk.app.model.impl.EncryptedCredentials;

@Service
public class UpdateService {

	private Map<String, EncryptedCredentials> registeredUsers = new HashMap<String, EncryptedCredentials>();

	public synchronized final boolean addUser(String regId, EncryptedCredentials encCredentials) {
		if (registeredUsers.put(regId, encCredentials) != null) {
			return true;
		}
		return false;
	}
	
	public synchronized final boolean removeUser(String regId, EncryptedCredentials encCredentials)
			throws CredentialException {
		if(!EqualsBuilder.reflectionEquals(registeredUsers.get(regId), encCredentials)){
			throw new CredentialException("wrong credentials for given user");
		}
		if (registeredUsers.remove(regId) != null) {
			return true;
		}
		return false;
	}

	/**
	 * @return the registeredUsers
	 */
	public synchronized final Map<String, EncryptedCredentials> getRegisteredUsers() {
		return this.registeredUsers;
	}

}
