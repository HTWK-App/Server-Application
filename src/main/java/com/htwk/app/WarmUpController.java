package com.htwk.app;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/warmup")
public class WarmUpController {

	private static final Logger logger = LoggerFactory.getLogger(WarmUpController.class);

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String home(HttpRequest request) throws URISyntaxException {
		return "home";
	}

}
