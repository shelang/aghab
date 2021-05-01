package io.shelang.aghab.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "link_expiration")
public class LinkExpiration {

  @Id
  @Column(name = "link_id")
  private Long linkId;

  @Column(name = "expire_at")
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

  public LinkExpiration setExpireAt(Instant expireAt) {
    this.expireAt = expireAt;
    return this;
  }
}
