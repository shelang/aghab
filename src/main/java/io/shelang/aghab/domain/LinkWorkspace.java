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
@Table(name = "link_workspace")
public class LinkWorkspace {

  @EmbeddedId
  private LinkWorkspaceId id;

  @Column(name = "link_id")
  private Long linkId;

  @Column(name = "create_at")
  private Instant createAt;

  @Embeddable
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  public static class LinkWorkspaceId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "link_hash")
    private String linkHash;
  }
}
