package com.htwk.app.repository;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.htwk.app.model.weather.WeatherData;
import com.htwk.app.model.weather.WeatherDay;
import com.htwk.app.model.weather.WeatherLocation;

@Repository
public class WeatherRepository {

  private static Logger logger = LoggerFactory.getLogger(WeatherRepository.class);

  @Autowired
  CacheManager cacheManager;

  RestTemplate restTemplate = null;
  HttpHeaders headers = null;

  private static Map<Integer, String> weatherStatus = new TreeMap<Integer, String>();
  static {
    weatherStatus.put(200, "Gewitter mit leichtem Regen");
    weatherStatus.put(201, "Gewitter mit Regen");
    weatherStatus.put(202, "Gewitter mit starkem Regen");
    weatherStatus.put(210, "leichte Gewitter");
    weatherStatus.put(211, "Gewitter");
    weatherStatus.put(212, "schwere Gewitter");
    weatherStatus.put(221, "einige Gewitter");
    weatherStatus.put(230, "Gewitter mit leichtem Nieselregen");
    weatherStatus.put(231, "Gewitter mit Nieselregen");
    weatherStatus.put(232, "Gewitter mit starkem Nieselregen");
    weatherStatus.put(300, "leichtes Nieseln");
    weatherStatus.put(301, "Nieseln");
    weatherStatus.put(302, "starkes Nieseln");
    weatherStatus.put(310, "leichter Nieselregen");
    weatherStatus.put(311, "Nieselregen");
    weatherStatus.put(312, "starker Nieselregen");
    weatherStatus.put(321, "Nieselschauer");
    weatherStatus.put(500, "leichter Regen");
    weatherStatus.put(501, "mäßiger Regen");
    weatherStatus.put(502, "sehr starker Regen");
    weatherStatus.put(503, "sehr starker Regen");
    weatherStatus.put(504, "Starkregen");
    weatherStatus.put(511, "Eisregen");
    weatherStatus.put(520, "leichte Regenschauer");
    weatherStatus.put(521, "Regenschauer");
    weatherStatus.put(522, "heftige Regenschauer");
    weatherStatus.put(600, "mäßiger Schnee");
    weatherStatus.put(601, "Schnee");
    weatherStatus.put(602, "heftiger Schneefall");
    weatherStatus.put(611, "Graupel");
    weatherStatus.put(621, "Schneeschauer");
    weatherStatus.put(701, "trüb");
    weatherStatus.put(711, "Rauch");
    weatherStatus.put(721, "Dunst");
    weatherStatus.put(731, "Sand / Staubsturm");
    weatherStatus.put(741, "Nebel");
    weatherStatus.put(800, "klarer Himmel");
    weatherStatus.put(801, "ein paar Wolken");
    weatherStatus.put(802, "überwiegend bewölkt");
    weatherStatus.put(803, "überwiegend bewölkt");
    weatherStatus.put(804, "wolkenbedeckt");
    weatherStatus.put(900, "Tornado");
    weatherStatus.put(901, "Tropensturm");
    weatherStatus.put(902, "Hurrikan");
    weatherStatus.put(903, "kalt");
    weatherStatus.put(904, "heiß");
    weatherStatus.put(905, "windig");
    weatherStatus.put(906, "Hagel");

    weatherStatus.put(950, "Einstellung");
    weatherStatus.put(951, "Windstille");
    weatherStatus.put(952, "Leichte Briese");
    weatherStatus.put(953, "Milde Briese");
    weatherStatus.put(954, "Mäßige Briese");
    weatherStatus.put(955, "Frische Briese");
    weatherStatus.put(956, "Starke Briese");
    weatherStatus.put(957, "Hochwind, annähender Sturm");
    weatherStatus.put(958, "Sturm");
    weatherStatus.put(959, "Schwerer Sturm");
    weatherStatus.put(960, "Gewitter");
    weatherStatus.put(961, "Heftiges Gewitter");
    weatherStatus.put(962, "Orkan");
  }

  @Value("${weather.forecast.url}")
  private String weatherForecastUrl;

  @Value("${weather.img.url}")
  private String weatherImageUrl;

  @Value("${weather.img.custom}")
  private String iconRootPath;

  @PostConstruct
  public void init() {
    restTemplate = new RestTemplate();
    headers = new HttpHeaders();
    headers.add("Content-Type", "text/xml;charset=utf-8");
    logger.debug("initialized WeatherRepository");
  }

  public Map<Long, Object> getWeather(String location, String days, boolean custom)
      throws JsonParseException, JsonMappingException, IOException, RestClientException,
      ParseException {

    String lat = "0";
    String lng = "0";

    if (location.contains(",")) {
      String latlng[] = location.split(",");
      lat = latlng[0];
      lng = latlng[1];
    }

    String uri = MessageFormat.format(weatherForecastUrl, days, "", lat, lng);
    ResponseEntity<String> response =
        restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);
    Map<Long, Object> weatherData = new HashMap<Long, Object>();
    if (response != null) {
      Gson gson = new Gson();
      JsonParser parser = new JsonParser();
      JsonObject obj = parser.parse(response.getBody()).getAsJsonObject();

      WeatherLocation locationObj =
          gson.fromJson(obj.get("city").getAsJsonObject(), WeatherLocation.class);
      weatherData.put((long) 0, locationObj);
      for (JsonElement element : obj.get("list").getAsJsonArray()) {
        WeatherDay day = gson.fromJson(element, WeatherDay.class);
        for (WeatherData weather : day.getWeather()) {
          weather.setDescriptionDe(getWeatherStatus(weather.getId()));
          if (custom) {
            weather.setIcon(getCustomWeatherImg(weather.getIcon()));
            weather.setIconData(getWeatherPic(weather.getIcon()));
          } else {
            weather.setIcon(weatherImageUrl + weather.getIcon());
            weather.setIconData(getWeatherPic(weather.getIcon()));
          }
        }
        weatherData.put(day.getDt(), day);
      }

      return weatherData;
    }
    return null;
  }

  private String getWeatherStatus(int id) {
    return weatherStatus.get(id);
  }

  public String getWeatherPic(String iconImgUrl) throws RestClientException, IOException,
      ParseException {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "image/png; charset=binary");
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    return "data:image/png;base64,"
        + Base64.encode(restTemplate.exchange(iconImgUrl, HttpMethod.GET, entity, byte[].class)
            .getBody());
  }

  private String getCustomWeatherImg(String icon) {
    String iconPath = iconRootPath;
    switch (icon) {
      case "01d": {
        iconPath += "32";
        break;
      }
      case "01n": {
        iconPath += "31";
        break;
      }
      case "02d": {
        iconPath += "30";
        break;
      }
      case "02n": {
        iconPath += "29";
        break;
      }
      case "03d": {
        iconPath += "26";
        break;
      }
      case "03n": {
        iconPath += "26";
        break;
      }
      case "04d": {
        iconPath += "28";
        break;
      }
      case "04n": {
        iconPath += "27";
        break;
      }
      case "09d": {
        iconPath += "40";
        break;
      }
      case "09n": {
        iconPath += "40";
        break;
      }
      case "10d": {
        iconPath += "39";
        break;
      }
      case "10n": {
        iconPath += "45";
        break;
      }
      case "11d": {
        iconPath += "38";
        break;
      }
      case "11n": {
        iconPath += "47";
        break;
      }
      case "13d": {
        iconPath += "10";
        break;
      }
      case "13n": {
        iconPath += "10";
        break;
      }
      case "50d": {
        iconPath += "22";
        break;
      }
      case "50n": {
        iconPath += "21";
        break;
      }
      default:
        iconPath += "na";
        break;
    }
    iconPath += ".png";
    return iconPath;
  }

  public String getWeatherForecastUrl() {
    return weatherForecastUrl;
  }

  public void setWeatherForecastUrl(String weatherForecastUrl) {
    this.weatherForecastUrl = weatherForecastUrl;
  }

  public String getWeatherImageUrl() {
    return weatherImageUrl;
  }

  public void setWeatherImageUrl(String weatherImageUrl) {
    this.weatherImageUrl = weatherImageUrl;
  }

  public String getIconRootPath() {
    return iconRootPath;
  }

  public void setIconRootPath(String iconRootPath) {
    this.iconRootPath = iconRootPath;
  }

}
