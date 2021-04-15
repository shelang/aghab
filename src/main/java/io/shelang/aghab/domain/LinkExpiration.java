package io.shelang.aghab.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "link_expiration")
public class LinkExpiration {

  @Id private Long linkId;
  private Instant expireAt;

  public Long getLinkId() {
    return linkId;
  }

  public LinkExpiration setLinkId(Long linkId) {
    this.linkId = linkId;
    return this;
  }

  public Instant getExpireAt() {
    return expireAt;
  }

  public LinkExpiration setExpireAt(Instant expire_date) {
    this.expireAt = expire_date;
    return this;
  }
}
