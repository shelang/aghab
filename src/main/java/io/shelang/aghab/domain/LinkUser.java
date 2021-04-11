package io.shelang.aghab.domain;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class LinkUser {

  @EmbeddedId
  private LinkUserId id;

  public LinkUser() {
  }

  public LinkUserId getId() {
    return id;
  }

  public LinkUser setId(LinkUserId id) {
    this.id = id;
    return this;
  }

  @Embeddable
  private static class LinkUserId implements Serializable {
    private Long userId;
    private String linkHash;

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
  }

}
