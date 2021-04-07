package io.shelang.aghab.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class LinkUser {

  @Id
  @GeneratedValue
  private Long id;
  private Long userId;
  private Long linkId;

  public Long getId() {
    return id;
  }

  public LinkUser setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public LinkUser setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public Long getLinkId() {
    return linkId;
  }

  public LinkUser setLinkId(Long linkId) {
    this.linkId = linkId;
    return this;
  }
}
