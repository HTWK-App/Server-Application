package com.htwk.app.repository;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.mail.Flags.Flag;
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

	public synchronized final List<Mail> getMails(String enryptedCredentials, String salt, int limit, int offset)
			throws MessagingException, IOException {
		EncryptedCredentials encCred = new EncryptedCredentials(enryptedCredentials, salt);
		MailCredentials credentials = new MailCredentials(authService.decryptCredentials(encCred));
		credentials.setProtocol(getProtocol);
		credentials.setHost(getHost);
		credentials.setPort(getPort);

		return receiver.getEmails(credentials, limit, offset);
	}

	public synchronized final List<Mail> getNewMails(String enryptedCredentials, String salt)
			throws MessagingException, IOException {
		EncryptedCredentials encCred = new EncryptedCredentials(enryptedCredentials, salt);
		MailCredentials credentials = new MailCredentials(authService.decryptCredentials(encCred));
		credentials.setProtocol(getProtocol);
		credentials.setHost(getHost);
		credentials.setPort(getPort);

		return receiver.getNewEmails(credentials);
	}

	public synchronized final void sendMail(Mail mail, String enryptedCredentials, String salt)
			throws MessagingException {
		EncryptedCredentials encCred = new EncryptedCredentials(enryptedCredentials, salt);
		MailCredentials credentials = new MailCredentials(authService.decryptCredentials(encCred));
		credentials.setProtocol(sendProtocol);
		credentials.setHost(sendHost);
		credentials.setPort(sendPort);

		receiver.sendMail(mail, credentials);
	}

	public synchronized final ResponseEntity<byte[]> getAttachment(int mail, String attachmentName,
			String enryptedCredentials, String salt) throws MessagingException, IOException {
		EncryptedCredentials encCred = new EncryptedCredentials(enryptedCredentials, salt);
		MailCredentials credentials = new MailCredentials(authService.decryptCredentials(encCred));
		credentials.setProtocol(getProtocol);
		credentials.setHost(getHost);
		credentials.setPort(getPort);

		return receiver.downloadAttachment(mail, attachmentName, credentials);
	}

	public synchronized final ResponseEntity<Mail> getMail(int mailId, String enryptedCredentials, String salt)
			throws MessagingException, IOException {
		EncryptedCredentials encCred = new EncryptedCredentials(enryptedCredentials, salt);
		MailCredentials credentials = new MailCredentials(authService.decryptCredentials(encCred));
		credentials.setProtocol(getProtocol);
		credentials.setHost(getHost);
		credentials.setPort(getPort);

		return new ResponseEntity(receiver.getEmail(mailId, credentials, salt), HttpStatus.OK);

	}

	public synchronized final boolean changeMailStatus(int mailId, String enryptedCredentials, String salt) {
		EncryptedCredentials encCred = new EncryptedCredentials(enryptedCredentials, salt);
		MailCredentials credentials = new MailCredentials(authService.decryptCredentials(encCred));
		credentials.setProtocol(getProtocol);
		credentials.setHost(getHost);
		credentials.setPort(getPort);

		try {
			receiver.changeMailStatus(mailId, Flag.SEEN, credentials);
			return true;
		} catch (MessagingException ex) {
			logger.error("error while setting flag for mail: {}", ex);
		}
		return false;
	}

}
