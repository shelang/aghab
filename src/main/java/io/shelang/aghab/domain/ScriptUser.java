package io.shelang.aghab.domain;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "script_user")
public class ScriptUser {

  @EmbeddedId private ScriptUserId id;

  @Column(name = "script_id", insertable = false, updatable = false)
  private Long scriptId;

  @Column(name = "user_id", insertable = false, updatable = false)
  private Long userId;

  @Embeddable
  @Getter
  @Setter
  @ToString
  public static class ScriptUserId implements Serializable {
    @Column(name = "script_id")
    private Long scriptId;

    @Column(name = "user_id")
    private Long userId;

    public ScriptUserId() {}

    public ScriptUserId(Long userId, Long scriptId) {
      this.userId = userId;
      this.scriptId = scriptId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ScriptUserId that = (ScriptUserId) o;
      return userId.equals(that.userId) && scriptId.equals(that.scriptId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(userId, scriptId);
    }
  }
}
