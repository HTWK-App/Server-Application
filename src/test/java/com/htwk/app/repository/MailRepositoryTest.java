package com.htwk.app.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.htwk.app.model.mail.MailCredentials;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class MailRepositoryTest {

	private static final Logger logger = LoggerFactory.getLogger(MailRepositoryTest.class);

	@Value("${mail.salt}")
	private String salt;

	@Value("${mail.secret}")
	private String secret;

	private TextEncryptor encryptor;
	private MailCredentials credentials;

	@Before
	public void init() {
		encryptor = Encryptors.text(secret, salt);
		credentials = new MailCredentials("test", "password");
	}

	@Test
	public void Test() {
		logger.info("salt:" + salt);

		String enryptedCredentials = encryptCredentials(credentials);
		String decryptedCredentials = decryptCredentials(enryptedCredentials).toString();
		logger.info("" + enryptedCredentials + ":" + decryptedCredentials);
		Assert.assertEquals(credentials.toString(), decryptCredentials(enryptedCredentials).toString());
	}

	@Test(expected = IllegalStateException.class)
	public void Test2() {
		logger.info("salt:" + salt);

		String enryptedCredentials = encryptCredentials(credentials);

		encryptor = Encryptors.text(secret, "1409e6adc68bfb40b62d66a4d60f30017785e1ecf04a420ce3239356c2cc9a80");

		String decryptedCredentials = decryptCredentials(enryptedCredentials).toString();
		logger.info("" + enryptedCredentials + ":" + decryptedCredentials);
		Assert.assertNotEquals(credentials.toString(), decryptCredentials(enryptedCredentials).toString());

	}

	public String encryptCredentials(MailCredentials credentials) {
		return encryptor.encrypt(credentials.toString());
	}

	public MailCredentials decryptCredentials(String enryptedCredentials) {
		String[] decrypted = encryptor.decrypt(enryptedCredentials).split(":");
		return new MailCredentials(decrypted[0], decrypted[1]);
	}

}
