package io.shelang.aghab.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "link_user")
public class LinkUser {

  @EmbeddedId private LinkUserId id;

  @Column(name = "link_id")
  private Long linkId;

  @Column(name = "create_at")
  private Instant createAt;

  public LinkUser() {}

  public LinkUser(Long userId, String linkHash) {
    this.id = new LinkUserId(userId, linkHash);
  }

  public LinkUserId getId() {
    return id;
  }

  public LinkUser setId(LinkUserId id) {
    this.id = id;
    return this;
  }

  public Long getLinkId() {
    return linkId;
  }

  public LinkUser setLinkId(Long linkId) {
    this.linkId = linkId;
    return this;
  }

  public Instant getCreateAt() {
    return createAt;
  }

  public LinkUser setCreateAt(Instant createAt) {
    this.createAt = createAt;
    return this;
  }

  @Embeddable
  public static class LinkUserId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "link_hash")
    private String linkHash;

    public LinkUserId() {}

    public LinkUserId(Long userId, String linkHash) {
      this.userId = userId;
      this.linkHash = linkHash;
    }

    public Long getUserId() {
      return userId;
    }

    public LinkUserId setUserId(Long userId) {
      this.userId = userId;
      return this;
    }

    public String getLinkHash() {
      return linkHash;
    }

    public LinkUserId setLinkHash(String linkHash) {
      this.linkHash = linkHash;
      return this;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      LinkUserId that = (LinkUserId) o;
      return userId.equals(that.userId) && linkHash.equals(that.linkHash);
    }

    @Override
    public int hashCode() {
      return Objects.hash(userId, linkHash);
    }
  }
}
