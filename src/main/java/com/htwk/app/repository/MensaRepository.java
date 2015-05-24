package com.htwk.app.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.naming.directory.InvalidAttributeValueException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.htwk.app.config.CredentialProperties;
import com.htwk.app.model.impl.Day;
import com.htwk.app.model.mensa.Meal;
import com.htwk.app.repository.helper.impl.MensaConverter;

@Repository
public class MensaRepository {

  private static Logger logger = LoggerFactory.getLogger(MensaRepository.class);

  @Autowired
  private CredentialProperties credProps;

  RestTemplate restTemplate = null;
  ResponseEntity<String> response = null;
  HttpHeaders headers = null;
  String uri = null;
  MensaConverter conv = null;

  SimpleDateFormat formatdate = new SimpleDateFormat("yyyyMMdd");

  @PostConstruct
  public void init() {
    restTemplate = new RestTemplate();
    headers = new HttpHeaders();
    headers.add("Content-Type", "text/xml;charset=UTF-8");
    headers.set("Authorization", "Basic " + credProps.getMensaAuth());
    conv = new MensaConverter();
    logger.debug("initialized MensaRepository");
  }

  @Value("${mensa.url}")
  private String mensaUrl;

  public Day<Meal> get(int location, String date) throws ParseException,
      InvalidAttributeValueException {
    if (formatdate.parse(date) == null) {
      throw new InvalidAttributeValueException("invalid dateformat (use yyyyMMdd)");
    }
    uri = mensaUrl + "?location=" + location + "&date=" + date;
    response =
        restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

    if (response.hasBody()) {
      Day<Meal> day = conv.getObject(response.getBody());
      if (day.getDayContent().isEmpty()) {
        throw new InvalidAttributeValueException("no entries found for given date");
      }
      day.setId(date);
      return day;
    }

    return null;
  }

  public Day<Meal> get(int location) throws ParseException, InvalidAttributeValueException {
    return get(location, formatdate.format(new Date()));
  }

  public String getMensaUrl() {
    return mensaUrl;
  }

  public void setMensaUrl(String mensaUrl) {
    this.mensaUrl = mensaUrl;
  }

}
