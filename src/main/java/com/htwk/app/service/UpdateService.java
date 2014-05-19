package com.htwk.app.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.security.auth.login.CredentialException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.htwk.app.model.impl.EncryptedCredentials;
import com.htwk.app.model.mail.Mail;
import com.htwk.app.repository.MailBoxRepository;
import com.htwk.app.utils.PushStatus;

@Service
public class UpdateService {

	private static final Logger logger = LoggerFactory.getLogger(UpdateService.class);

	@Autowired
	private GCMService gcmService;

	@Autowired
	private MailBoxRepository mailRepo;

	private Map<String, EncryptedCredentials> registeredUsers = new HashMap<String, EncryptedCredentials>();

	public synchronized final boolean addUser(String regId, EncryptedCredentials encCredentials) {
		if (registeredUsers.put(regId, encCredentials) != null) {
			logger.info("" + regId);
			return true;
		}
		return false;
	}

	public synchronized final boolean removeUser(String regId, EncryptedCredentials encCredentials)
			throws CredentialException {
		if (!EqualsBuilder.reflectionEquals(registeredUsers.get(regId), encCredentials)) {
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

	public Map<String, String> test(String regId) throws MessagingException, IOException {
		Map<String, String> response = new HashMap<String, String>();
		for (Entry<String, EncryptedCredentials> entry : registeredUsers.entrySet()) {
			if (entry.getKey().equals(regId)) {

				List<Mail> mails = mailRepo.getNewMails(entry.getValue().getEncryptedCredentials(), entry.getValue()
						.getSalt());

				if (!mails.isEmpty()) {
					response.put(entry.getKey(), gcmService.test(entry.getKey(), PushStatus.NEW_MAILS));
				}
			}
		}
		return response;
	}

}
