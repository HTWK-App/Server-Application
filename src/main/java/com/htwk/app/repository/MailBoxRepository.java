package com.htwk.app.repository;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.htwk.app.model.impl.EncryptedCredentials;
import com.htwk.app.model.mail.Mail;
import com.htwk.app.model.mail.MailCredentials;
import com.htwk.app.repository.helper.impl.EmailReceiver;
import com.htwk.app.service.AuthenticationService;

@Repository
public class MailBoxRepository {

	private static final Logger logger = LoggerFactory.getLogger(MailBoxRepository.class);

	private EmailReceiver receiver;

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

	@Autowired
	private AuthenticationService authService;

	@PostConstruct
	public void init() {
		receiver = new EmailReceiver();
	}

	@Deprecated
	public List<Mail> getMails(String enryptedCredentials, int offset) throws MessagingException, IOException {
		MailCredentials credentials = new MailCredentials(authService.decryptCredentials(enryptedCredentials));
		credentials.setProtocol(getProtocol);
		credentials.setHost(getHost);
		credentials.setPort(getPort);

		return receiver.getEmails(credentials, offset);
	}

	public List<Mail> getMails(String enryptedCredentials, String salt, int offset) throws MessagingException,
			IOException {
		EncryptedCredentials encCred = new EncryptedCredentials(enryptedCredentials, salt);
		MailCredentials credentials = new MailCredentials(authService.decryptCredentials(encCred));
		credentials.setProtocol(getProtocol);
		credentials.setHost(getHost);
		credentials.setPort(getPort);

		return receiver.getEmails(credentials, offset);
	}

	@Deprecated
	public List<Mail> getNewMails(String enryptedCredentials) throws MessagingException, IOException {
		MailCredentials credentials = new MailCredentials(authService.decryptCredentials(enryptedCredentials));
		credentials.setProtocol(getProtocol);
		credentials.setHost(getHost);
		credentials.setPort(getPort);

		return receiver.getNewEmails(credentials);
	}

	public List<Mail> getNewMails(String enryptedCredentials, String salt) throws MessagingException, IOException {
		EncryptedCredentials encCred = new EncryptedCredentials(enryptedCredentials, salt);
		MailCredentials credentials = new MailCredentials(authService.decryptCredentials(encCred));
		credentials.setProtocol(getProtocol);
		credentials.setHost(getHost);
		credentials.setPort(getPort);

		return receiver.getNewEmails(credentials);
	}

	@Deprecated
	public void sendMail(Mail mail, String enryptedCredentials) throws MessagingException {
		MailCredentials credentials = new MailCredentials(authService.decryptCredentials(enryptedCredentials));
		credentials.setProtocol(sendProtocol);
		credentials.setHost(sendHost);
		credentials.setPort(sendPort);

		receiver.sendMail(mail, credentials);
	}

	public void sendMail(Mail mail, String enryptedCredentials, String salt) throws MessagingException {
		EncryptedCredentials encCred = new EncryptedCredentials(enryptedCredentials, salt);
		MailCredentials credentials = new MailCredentials(authService.decryptCredentials(encCred));
		credentials.setProtocol(sendProtocol);
		credentials.setHost(sendHost);
		credentials.setPort(sendPort);

		receiver.sendMail(mail, credentials);
	}

	@Deprecated
	public ResponseEntity<byte[]> getAttachment(int mail, String attachmentName, String enryptedCredentials)
			throws MessagingException, IOException {
		MailCredentials credentials = new MailCredentials(authService.decryptCredentials(enryptedCredentials));
		credentials.setProtocol(getProtocol);
		credentials.setHost(getHost);
		credentials.setPort(getPort);

		return receiver.downloadAttachment(mail, attachmentName, credentials);
	}

	public ResponseEntity<byte[]> getAttachment(int mail, String attachmentName, String enryptedCredentials, String salt)
			throws MessagingException, IOException {
		EncryptedCredentials encCred = new EncryptedCredentials(enryptedCredentials, salt);
		MailCredentials credentials = new MailCredentials(authService.decryptCredentials(encCred));
		credentials.setProtocol(getProtocol);
		credentials.setHost(getHost);
		credentials.setPort(getPort);

		return receiver.downloadAttachment(mail, attachmentName, credentials);
	}

	public ResponseEntity<Mail> getMail(int mailId, String enryptedCredentials, String salt) throws MessagingException, IOException {
		EncryptedCredentials encCred = new EncryptedCredentials(enryptedCredentials, salt);
		MailCredentials credentials = new MailCredentials(authService.decryptCredentials(encCred));
		credentials.setProtocol(getProtocol);
		credentials.setHost(getHost);
		credentials.setPort(getPort);

		return new ResponseEntity(receiver.getEmail(mailId, credentials, salt), HttpStatus.OK);

	}

}
