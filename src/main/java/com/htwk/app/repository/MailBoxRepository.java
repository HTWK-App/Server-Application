package com.htwk.app.repository;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Repository;

import com.htwk.app.model.mail.Mail;
import com.htwk.app.model.mail.MailCredentials;
import com.htwk.app.repository.helper.impl.EmailReceiver;

@Repository
public class MailBoxRepository {

	private static final Logger logger = LoggerFactory.getLogger(MailBoxRepository.class);

	private TextEncryptor encryptor;
	private EmailReceiver receiver;

	@Value("${mail.salt}") 
	private String salt;

	@Value("${mail.secret}")
	private String secret;
	
	@Value("${mail.get.protocol}")
	private String getProtocol;
	
	@Value("${mail.get.host}")
	private String getHost;
	
	@Value("${mail.get.port}")
	private int getPort;
	
	@Value("${mail.send.protocol}")
	private String sendProtocol;
	
	@Value("${mail.send.host}")
	private String sendHost;
	
	@Value("${mail.send.port}")
	private int sendPort;

	@PostConstruct
	public void init() {
		encryptor = Encryptors.text(secret, salt);
		receiver = new EmailReceiver();
	}

	public List<Mail> getMails(String enryptedCredentials, int offset) throws MessagingException, IOException {
		MailCredentials credentails = decryptCredentials(enryptedCredentials);
		credentails.setProtocol(getProtocol);
		credentails.setHost(getHost);
		credentails.setPort(getPort);

		return receiver.getEmails(credentails, offset);
	}

	public List<Mail> getNewMails(String enryptedCredentials) throws MessagingException, IOException {
		MailCredentials credentails = decryptCredentials(enryptedCredentials);
		credentails.setProtocol(getProtocol);
		credentails.setHost(getHost);
		credentails.setPort(getPort);
		
		return receiver.getNewEmails(credentails);
	}
	
	public void sendMail(Mail mail, String enryptedCredentials) throws MessagingException{
		MailCredentials credentails = decryptCredentials(enryptedCredentials);
		credentails.setProtocol(sendProtocol);
		credentails.setHost(sendHost);
		credentails.setPort(sendPort);
		
		receiver.sendMail(mail, credentails);
	}

	public ResponseEntity<byte[]> getAttachment(int mail, String attachmentName, String enryptedCredentials)
			throws MessagingException, IOException {
		MailCredentials credentails = decryptCredentials(enryptedCredentials);
		credentails.setProtocol(getProtocol);
		credentails.setHost(getHost);
		credentails.setPort(getPort);
		
		return receiver.downloadAttachment(mail, attachmentName, credentails);
	}
	
	public String encryptCredentials(MailCredentials credentials) {
		return encryptor.encrypt(credentials.toString());
	}

	private MailCredentials decryptCredentials(String enryptedCredentials) {
		String[] decrypted = encryptor.decrypt(enryptedCredentials).split(":");
		return new MailCredentials(decrypted[0], decrypted[1]);
	}

}
