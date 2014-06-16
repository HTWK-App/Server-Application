package com.htwk.app.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.security.auth.login.CredentialException;

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

	private List<String> registeredUsers = new ArrayList<String>();

	public synchronized final boolean addUser(String regId) {
		if (registeredUsers.add(regId)) {
			logger.info("" + regId);
			return true;
		}
		return false;
	}

	public synchronized final boolean removeUser(String regId) throws CredentialException {
		if (!registeredUsers.contains(regId)) {
			throw new CredentialException("wrong credentials for given user");
		}
		if (registeredUsers.remove(regId)) {
			return true;
		}
		return false;
	}

	public Map<String, String> addUserCredentials(String regId, EncryptedCredentials encCredentials)
			throws MessagingException, IOException {
		Map<String, String> response = new HashMap<String, String>();

		List<Mail> mails = mailRepo.getNewMails(encCredentials.getEncryptedCredentials(), encCredentials.getSalt());

		if (!mails.isEmpty()) {
			response.put(regId, gcmService.send(regId, PushStatus.NEW_MAILS));
		}
		return response;
	}
	
	public String sendPushRequest(String regId){
		return gcmService.send(regId, PushStatus.PUSH_REQUEST);
	}
	/**
	 * @return the registeredUsers
	 */
	public synchronized final List<String> getRegisteredUsers() {
		return this.registeredUsers;
	}
}
