package io.shelang.aghab.domain;

import java.io.Serializable;
import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "link_user")
public class LinkUser {

  @EmbeddedId
  private LinkUserId id;

  @Column(name = "user_id", updatable = false, insertable = false)
  private Long userId;

  @Column(name = "link_hash", updatable = false, insertable = false)
  private String linkHash;

  @Column(name = "link_id")
  private Long linkId;

  @Column(name = "create_at")
  private Instant createAt;

  public LinkUser(Long userId, String linkHash) {
    this.id = new LinkUserId(userId, linkHash);
  }

  @Embeddable
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode
  public static class LinkUserId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "link_hash")
    private String linkHash;
  }
}
