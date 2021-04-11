package io.shelang.aghab.domain;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LinkAnalytics {

  @Id
  private Long linkId;
  private String platform;
  private String device;
  private String browser;
  private String location;
  private String ip;
  private Instant createdAt;

  public Long getLinkId() {
    return linkId;
  }

  public LinkAnalytics setLinkId(Long linkId) {
    this.linkId = linkId;
    return this;
  }

  public String getPlatform() {
    return platform;
  }

  public LinkAnalytics setPlatform(String platform) {
    this.platform = platform;
    return this;
  }

  public String getDevice() {
    return device;
  }

  public LinkAnalytics setDevice(String device) {
    this.device = device;
    return this;
  }

  public String getBrowser() {
    return browser;
  }

  public LinkAnalytics setBrowser(String browser) {
    this.browser = browser;
    return this;
  }

  public String getLocation() {
    return location;
  }

  public LinkAnalytics setLocation(String location) {
    this.location = location;
    return this;
  }

  public String getIp() {
    return ip;
  }

  public LinkAnalytics setIp(String ip) {
    this.ip = ip;
    return this;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public LinkAnalytics setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
    return this;
  }
}
