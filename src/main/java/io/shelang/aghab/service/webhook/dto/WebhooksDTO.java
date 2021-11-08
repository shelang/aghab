package io.shelang.aghab.service.webhook.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class WebhooksDTO {

    private List<WebhookDTO> webhooks;

}
