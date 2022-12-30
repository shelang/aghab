package io.shelang.aghab.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
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
