package io.shelang.aghab.service.webhook;

import io.shelang.aghab.service.dto.webhook.WebhookAPICallDTO;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.Response;

public interface SimplePostAPI {

  @POST
  Response executePost(WebhookAPICallDTO body);
}
