package io.shelang.aghab.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "webhook_link")
public class WebhookLink {

  @EmbeddedId private WebhookLinkId id;
  private Integer count;

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

    public WebhookLinkId() {}

    public WebhookLinkId(Long linkId, Long webhookId) {
      this.linkId = linkId;
      this.webhookId = webhookId;
    }
  }
}
