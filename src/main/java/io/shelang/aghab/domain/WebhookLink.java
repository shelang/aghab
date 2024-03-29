package io.shelang.aghab.domain;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "webhook_link")
public class WebhookLink {

  @EmbeddedId
  private WebhookLinkId id;
  private Long count;

  @Embeddable
  @Getter
  @Setter
  @ToString
  @EqualsAndHashCode
  public static class WebhookLinkId implements Serializable {

    @Column(name = "webhook_id")
    private Long webhookId;

    @Column(name = "link_id")
    private Long linkId;

    public WebhookLinkId() {
    }

    public WebhookLinkId(Long linkId, Long webhookId) {
      this.linkId = linkId;
      this.webhookId = webhookId;
    }
  }
}
