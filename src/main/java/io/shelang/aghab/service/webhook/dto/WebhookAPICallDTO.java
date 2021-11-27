package io.shelang.aghab.service.webhook.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class WebhookAPICallDTO {
  private String hash;
  private Instant date;
}
