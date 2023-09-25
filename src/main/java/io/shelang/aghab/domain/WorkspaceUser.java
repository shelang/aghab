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
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Accessors(chain = true)
@Table(name = "workspace_user")
public class WorkspaceUser {

  @EmbeddedId
  private WorkspaceUserId id;

  @Column(name = "workspace_id", insertable = false, updatable = false)
  private Long workspaceId;

  @Column(name = "user_id", insertable = false, updatable = false)
  private Long userId;

  @Column(name = "create_at")
  @CreationTimestamp
  private Instant createAt;

  @Embeddable
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class WorkspaceUserId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "workspace_id")
    private Long workspaceId;

  }
}
