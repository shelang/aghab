package io.shelang.aghab.service.dto;

import io.shelang.aghab.enums.LinkStatus;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

public class LinkCreateDTO {

  @NotBlank private String url;
  private String hash;
  private Instant expireAt;

  private LinkStatus status = LinkStatus.ACTIVE;

  @Min(150)
  private String title;

  @Min(255)
  private String description;

  public LinkCreateDTO() {}

  public String getUrl() {
    return url;
  }

  public LinkCreateDTO setUrl(String url) {
    this.url = url;
    return this;
  }

  public String getHash() {
    return hash;
  }

  public LinkCreateDTO setHash(String hash) {
    this.hash = hash;
    return this;
  }

  public Instant getExpireAt() {
    return expireAt;
  }

  public LinkCreateDTO setExpireAt(Instant expireAt) {
    this.expireAt = expireAt;
    return this;
  }

  public LinkStatus getStatus() {
    return status;
  }

  public LinkCreateDTO setStatus(LinkStatus status) {
    this.status = status;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public LinkCreateDTO setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public LinkCreateDTO setDescription(String description) {
    this.description = description;
    return this;
  }
}
