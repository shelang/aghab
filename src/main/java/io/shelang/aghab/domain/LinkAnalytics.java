package io.shelang.aghab.domain;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "link_analytics")
public class LinkAnalytics {

  @Id
  @Column(name = "link_id")
  private Long linkId;

  private String platform;
  private String device;
  private String browser;
  private String location;
  private String ip;

  @CreationTimestamp
  @Column(name = "create_at")
  private Instant createAt;

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
    return createAt;
  }

  public LinkAnalytics setCreatedAt(Instant createAt) {
    this.createAt = createAt;
    return this;
  }
}
