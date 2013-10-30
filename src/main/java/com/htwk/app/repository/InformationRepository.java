package com.htwk.app.repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.naming.directory.InvalidAttributesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.htwk.app.model.info.Staff;
import com.htwk.app.repository.helper.impl.InformationConverter;

@Repository
public class InformationRepository {

	private static final Logger logger = LoggerFactory.getLogger(InformationRepository.class);

	RestTemplate restTemplate = null;
	ResponseEntity<String> response = null;
	HttpHeaders headers = null;
	URI uri = null;
	InformationConverter conv = null;

	@Value("${info.staff.url}")
	private String staffUrl;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.add("Content-Type", "text/xml;charset=UTF-8");
		conv = new InformationConverter();
	}

	@Cacheable("timeCache")
	public List<Staff> getStaff() throws URISyntaxException, InvalidAttributesException {
		staffUrl = MessageFormat.format(staffUrl, "");

		uri = new URI(staffUrl);
		logger.debug("getData from URI: " + uri.toString());
		response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

		if (response != null) {
			return conv.getStaffList(response.getBody());
			// return response.getBody();
		}

		return null;
	}

	@Cacheable("timeCache")
	public Staff getStaff(String cuid) throws InvalidAttributesException, URISyntaxException {
		List<Staff> staffMembers = getStaff();
//		int i = 0;
		for (Staff staff : staffMembers) {
			if (staff.getCuid().equals(cuid)) {
//				return staffMembers.subList(i, i+1);
				return staff;
			}
//			i++;
		}
		return null;
	}

	public Staff getStaffDetailed(String cuid) throws InvalidAttributesException, URISyntaxException, IOException, ParseException {
		return conv.getStaffDetailed(getStaff(cuid));
	}

}
