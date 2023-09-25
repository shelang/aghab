package io.shelang.aghab.domain;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "webhook_user")
public class WebhookUser {

  @EmbeddedId
  private WebhookUserId id;

  @Column(name = "webhook_id", insertable = false, updatable = false)
  private Long webhookId;

  @Column(name = "user_id", insertable = false, updatable = false)
  private Long userId;

  @Embeddable
  @Getter
  @Setter
  @ToString
  public static class WebhookUserId implements Serializable {

    @Column(name = "webhook_id")
    private Long webhookId;

    @Column(name = "user_id")
    private Long userId;

    public WebhookUserId() {
    }

    public WebhookUserId(Long userId, Long webhookId) {
      this.userId = userId;
      this.webhookId = webhookId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      WebhookUserId that = (WebhookUserId) o;
      return userId.equals(that.userId) && webhookId.equals(that.webhookId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(userId, webhookId);
    }
  }
}
