package io.shelang.aghab.service.dto.webhook;

import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class WebhookAPICallDTO {

  private Long LinkId;
  private String hash;
  private Instant date;

}
