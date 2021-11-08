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
@Table(name = "webhook_user")
public class WebhookUser {

  @EmbeddedId private WebhookUserId id;

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

    public WebhookUserId() {}

    public WebhookUserId(Long userId, Long webhookId) {
      this.userId = userId;
      this.webhookId = webhookId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      WebhookUserId that = (WebhookUserId) o;
      return userId.equals(that.userId) && webhookId.equals(that.webhookId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(userId, webhookId);
    }
  }
}
