package com.htwk.app.controller;

import javax.security.auth.login.CredentialException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.htwk.app.model.impl.Credentials;
import com.htwk.app.model.impl.EncryptedCredentials;
import com.htwk.app.service.AuthenticationService;
import com.htwk.app.service.GCMService;
import com.htwk.app.service.UpdateService;

@Controller
@RequestMapping(value = "/auth")
public class AuthenticationController {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private AuthenticationService service;

	@Autowired
	private UpdateService updateService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public @ResponseBody
	EncryptedCredentials encryptCredentials(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password) {
		Credentials credentials = new Credentials();
		credentials.setUsername(username);
		credentials.setPassword(password);
		return service.encryptCredentials(credentials);
	}

	@RequestMapping(value = "/push", method = RequestMethod.POST)
	public @ResponseBody
	String enablePushNotification(@RequestParam(value = "credentials") String encryptedCredentials,
			@RequestParam(value = "salt") String salt, @RequestParam(value = "regid") String regid) {
		EncryptedCredentials encCredentials = new EncryptedCredentials(encryptedCredentials, salt);
		return "" + updateService.addUser(regid, encCredentials);
	}

	@RequestMapping(value = "/push", method = RequestMethod.DELETE)
	public @ResponseBody
	String disablePushNotification(@RequestParam(value = "credentials") String encryptedCredentials,
			@RequestParam(value = "salt") String salt, @RequestParam(value = "regid") String regid)
			throws CredentialException {
		EncryptedCredentials encCredentials = new EncryptedCredentials(encryptedCredentials, salt);
		return "" + updateService.removeUser(regid, encCredentials);
	}
	
	@RequestMapping(value = "/push/delete", method = RequestMethod.GET)
	public @ResponseBody
	String disablePushNotificationViaGet(@RequestParam(value = "credentials") String encryptedCredentials,
			@RequestParam(value = "salt") String salt, @RequestParam(value = "regid") String regid)
			throws CredentialException {
		EncryptedCredentials encCredentials = new EncryptedCredentials(encryptedCredentials, salt);
		return "" + updateService.removeUser(regid, encCredentials);
	}
}
