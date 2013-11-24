package com.htwk.app.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.htwk.app.model.mail.Mail;
import com.htwk.app.repository.MailBoxRepository;

@Controller
@RequestMapping(value = "/mailbox")
public class MailBoxController {

	private static final Logger logger = LoggerFactory.getLogger(MailBoxController.class);

	@Autowired
	private MailBoxRepository repo;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String home() {
		return "";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/mailbox";
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody
	List<Mail> getMails(@RequestParam(value = "credentials") String enryptedCredentials,
			@RequestParam(value = "offset", required = false, defaultValue = "10") int offset)
			throws MessagingException, IOException {
		return repo.getMails(enryptedCredentials, offset);
	}

	@RequestMapping(value = "/get/{mailId}", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<byte[]> getMailAttachment(@PathVariable(value = "mailId") int mailId,
			@RequestParam(value = "attachmentName") String attachmentName,
			@RequestParam(value = "credentials") String enryptedCredentials) throws MessagingException, IOException {
		logger.debug("" + attachmentName);
		return repo.getAttachment(mailId, attachmentName, enryptedCredentials);
	}

	@RequestMapping(value = "/get/new", method = RequestMethod.GET)
	public @ResponseBody
	List<Mail> getMails(@RequestParam(value = "credentials") String enryptedCredentials) throws MessagingException,
			IOException {
		return repo.getNewMails(enryptedCredentials);
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public @ResponseBody
	String sendMails(@RequestParam(value = "credentials") String enryptedCredentials,
			@RequestParam(value = "to") String[] to,
			@RequestParam(value = "cc", required = false, defaultValue = "") String[] cc,
			@RequestParam(value = "subject") String subject, @RequestParam(value = "message") String message)
			throws MessagingException, IOException {
		Mail mail = new Mail();
		mail.setToList(Arrays.asList(to));
		mail.setCcList(Arrays.asList(cc));
		mail.setSubject(subject);
		mail.setMessage(message);
		repo.sendMail(mail, enryptedCredentials);
		return "";
	}
}
