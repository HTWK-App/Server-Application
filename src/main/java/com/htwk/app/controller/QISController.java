package com.htwk.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;

import com.htwk.app.model.qis.Semester;
import com.htwk.app.repository.QISRepository;

@Controller
@RequestMapping(value = "/qis")
public class QISController {

	private static final Logger logger = LoggerFactory.getLogger(QISController.class);

	@Autowired
	private QISRepository repo;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String home() {
		return "";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/qis";
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody
	List<Semester> getGISData(@RequestParam(value = "credentials") String enryptedCredentials,
			@RequestParam(value = "salt", required = false, defaultValue = "") String salt) throws RestClientException,
			UnsupportedEncodingException {
		if (salt.isEmpty()) {
			return repo.getQISData(enryptedCredentials);
		}
		return repo.getQISData(enryptedCredentials, salt);
	}
}
