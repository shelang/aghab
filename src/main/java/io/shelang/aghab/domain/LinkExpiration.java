package io.shelang.aghab.domain;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class LinkExpiration {

  @Id
  @GeneratedValue
  private Long id;
  private Long linkId;
  private Instant expire_date;

  public Long getId() {
    return id;
  }

  public LinkExpiration setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getLinkId() {
    return linkId;
  }

  public LinkExpiration setLinkId(Long linkId) {
    this.linkId = linkId;
    return this;
  }

  public Instant getExpire_date() {
    return expire_date;
  }

  public LinkExpiration setExpire_date(Instant expire_date) {
    this.expire_date = expire_date;
    return this;
  }
}
