package com.htwk.app.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class MensaRepository {

	private static final Logger logger = LoggerFactory.getLogger(MensaRepository.class);

	public String get() {
		return "test";
	}

}
