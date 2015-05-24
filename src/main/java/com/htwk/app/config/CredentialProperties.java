package com.htwk.app.config;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(locations = "classpath:credentials.properties")
@PropertySource("classpath:credentials.properties")
public class CredentialProperties {

  @NotBlank
  @Value("${mensa.auth}")
  private String mensaAuth;
  
  @NotBlank
  @Value("${gcm.uri}")
  private String gcmUri;
  
  @NotBlank
  @Value("${gcm.apikey}")
  private String gcmApiKey;
  
  @NotBlank
  @Value("${gcm.senderid}")
  private String gcmSenderid;

  public String getMensaAuth() {
    return mensaAuth;
  }

  public void setMensaAuth(String mensaAuth) {
    this.mensaAuth = mensaAuth;
  }

  public String getGcmUri() {
    return gcmUri;
  }

  public void setGcmUri(String gcmUri) {
    this.gcmUri = gcmUri;
  }

  public String getGcmApiKey() {
    return gcmApiKey;
  }

  public void setGcmApiKey(String gcmApiKey) {
    this.gcmApiKey = gcmApiKey;
  }

  public String getGcmSenderid() {
    return gcmSenderid;
  }

  public void setGcmSenderid(String gcmSenderid) {
    this.gcmSenderid = gcmSenderid;
  }
  
}
