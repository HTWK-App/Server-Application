package com.htwk.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import com.htwk.app.model.impl.Credentials;
import com.htwk.app.model.impl.EncryptedCredentials;

@Service
public class AuthenticationService {

	@Value("${mail.salt}")
	private String salt;

	@Value("${mail.secret}")
	private String secret;

	public EncryptedCredentials encryptCredentials(Credentials credentials) {
		String salt = KeyGenerators.string().generateKey();
		TextEncryptor enc = Encryptors.text(secret, salt);
		return new EncryptedCredentials(enc.encrypt(credentials.toString()), salt);
	}

	public Credentials decryptCredentials(EncryptedCredentials encCred) {
		TextEncryptor enc = Encryptors.text(secret, encCred.getSalt());
		String[] decrypted = enc.decrypt(encCred.getEncryptedCredentials()).split(":");
		return new Credentials(decrypted[0], decrypted[1]);
	}

}
