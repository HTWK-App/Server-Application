package com.htwk.app.controller;

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

@Controller
@RequestMapping(value = "/auth")
public class AuthenticationController {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private AuthenticationService service;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public @ResponseBody
	EncryptedCredentials encryptCredentials(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password) {
		Credentials credentials = new Credentials();
		credentials.setUsername(username);
		credentials.setPassword(password);
		return service.encryptCredentials(credentials);
	}
}
