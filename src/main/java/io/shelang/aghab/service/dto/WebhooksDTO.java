package io.shelang.aghab.service.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class WebhooksDTO {

  private List<WebhookDTO> webhooks;

}
