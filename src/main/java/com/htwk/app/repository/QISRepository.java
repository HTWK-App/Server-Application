package com.htwk.app.repository;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.htwk.app.model.impl.Credentials;
import com.htwk.app.model.mail.MailCredentials;
import com.htwk.app.model.qis.Semester;
import com.htwk.app.repository.helper.impl.QISConverter;

@Repository
public class QISRepository {

	private static final Logger logger = LoggerFactory.getLogger(QISRepository.class);

	RestTemplate restTemplate = null;
	HttpHeaders headers = null;
	URI uri = null;
	QISConverter conv = null;
	private TextEncryptor encryptor;
	
	@Value("${mail.salt}") 
	private String salt;

	@Value("${mail.secret}")
	private String secret;
	
	@Value("${qis.url}")
	private String qisUrl;
	
	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		conv = new QISConverter();
		encryptor = Encryptors.text(secret, salt);
	}
	
	public List<Semester> getQISData(String enryptedCredentials) throws RestClientException, UnsupportedEncodingException {

		Credentials credentials = decryptCredentials(enryptedCredentials);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("username", ""+credentials.getUsername());
		map.add("password", ""+credentials.getPassword());
		map.add("submit", "Anmelden");
		
		//get authenticated
		ResponseEntity<String> response = restTemplate.postForEntity(qisUrl, map, String.class);

		String loggedinUrl = response.getHeaders().getLocation().toString();
		String asi = getQueryMap(loggedinUrl).get("asi");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Cookie", "JSESSIONID=" + getSessionId(response.getHeaders()));
		//get link to detailed view
		response = restTemplate
				.exchange(
						"https://qisserver.htwk-leipzig.de/qisserver/rds?state=notenspiegelStudent&next=tree.vm&nextdir=qispos/notenspiegel/student&navigationPosition=functions%2CnotenspiegelStudent&breadcrumb=notenspiegel&topitem=functions&subitem=notenspiegelStudent&asi="
								+ asi, HttpMethod.GET, new HttpEntity<String>(headers), String.class);

		//get mark
		response = restTemplate.exchange(getDetailLink(response.getBody().toString()),
				HttpMethod.GET, new HttpEntity<String>(headers), String.class);

		return conv.getSemesterAsList(response.getBody().toString());
	}

	private String getSessionId(HttpHeaders headers) {
		List<String> cookies = headers.get("Cookie");

		// assuming only one cookie with jsessionid as the only value
		if (cookies == null) {
			cookies = headers.get("Set-Cookie");
		}
		String cookie = cookies.get(cookies.size() - 1);
		int start = cookie.indexOf('=');
		int end = cookie.indexOf(';');

		return cookie.substring(start + 1, end);
	}

	public static String getDetailLink(String content) throws UnsupportedEncodingException {
		String temp = content.substring(
				content.indexOf("class=\"Konto\" href=") + "class=\"Konto\" href=".length(),
				content.indexOf(" title=\"Leistungen anzeigen\""));
		return URLDecoder.decode(temp.replace("&amp;", "&"), "UTF-8");
	}

	public Map<String, String> getQueryMap(String url) {
		String[] params = url.split("[&]");
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params) {
			String[] para = param.split("=");
			if (para.length >= 2) {
				map.put(para[0], para[1]);
			}
		}
		return map;
	}
	
	public String encryptCredentials(Credentials credentials) {
		return encryptor.encrypt(credentials.toString());
	}

	private Credentials decryptCredentials(String enryptedCredentials) {
		String[] decrypted = encryptor.decrypt(enryptedCredentials).split(":");
		return new Credentials(decrypted[0], decrypted[1]);
	}

}
