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
@Table(name = "link_workspace")
public class LinkWorkspace {

  @EmbeddedId
  private LinkWorkspaceId id;

  @Column(name = "link_id")
  private Long linkId;

  @Column(name = "workspace_id", updatable = false, insertable = false)
  private Long workspaceId;

  @Column(name = "link_hash", updatable = false, insertable = false)
  private String linkHash;

  @Column(name = "create_at")
  private Instant createAt;

  public LinkWorkspace(Long workspaceId, String linkHash) {
    this.id = new LinkWorkspaceId(workspaceId, linkHash);
  }

  @Embeddable
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  public static class LinkWorkspaceId implements Serializable {

    @Column(name = "workspace_id")
    private Long workspaceId;

    @Column(name = "link_hash")
    private String linkHash;
  }
}
