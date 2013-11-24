package com.htwk.app.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import com.htwk.app.model.impl.Credentials;

@Service
public class AuthenticationService {

	private TextEncryptor encryptor;
	
	@Value("${mail.salt}") 
	private String salt;

	@Value("${mail.secret}")
	private String secret;

	@PostConstruct
	public void init() {
		encryptor = Encryptors.text(secret, salt);
	}
	
	public String encryptCredentials(Credentials credentials) {
		return encryptor.encrypt(credentials.toString());
	}

	public Credentials decryptCredentials(String enryptedCredentials) {
		String[] decrypted = encryptor.decrypt(enryptedCredentials).split(":");
		return new Credentials(decrypted[0], decrypted[1]);
	}

	
}
