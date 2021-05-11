package io.shelang.aghab.service.dto;

import java.time.Instant;

public class LinkUserDTO {

  private Long userId;
  private Long linkId;
  private String linkHash;
  private Instant createAt;

  public Long getUserId() {
    return userId;
  }

  public LinkUserDTO setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public Long getLinkId() {
    return linkId;
  }

  public LinkUserDTO setLinkId(Long linkId) {
    this.linkId = linkId;
    return this;
  }

  public String getLinkHash() {
    return linkHash;
  }

  public LinkUserDTO setLinkHash(String linkHash) {
    this.linkHash = linkHash;
    return this;
  }

  public Instant getCreateAt() {
    return createAt;
  }

  public LinkUserDTO setCreateAt(Instant createAt) {
    this.createAt = createAt;
    return this;
  }
}
