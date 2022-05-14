package io.shelang.aghab.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "link_user")
public class LinkUser {

  @EmbeddedId
  private LinkUserId id;

  @Column(name = "link_id")
  private Long linkId;

  @Column(name = "create_at")
  private Instant createAt;

  public LinkUser(Long userId, String linkHash) {
    this.id = new LinkUserId(userId, linkHash);
  }

  @Embeddable
  @Getter
  @Setter
  @ToString
  public static class LinkUserId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "link_hash")
    private String linkHash;

    public LinkUserId() {
    }

    public LinkUserId(Long userId, String linkHash) {
      this.userId = userId;
      this.linkHash = linkHash;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      LinkUserId that = (LinkUserId) o;
      return userId.equals(that.userId) && linkHash.equals(that.linkHash);
    }

    @Override
    public int hashCode() {
      return Objects.hash(userId, linkHash);
    }
  }
}
