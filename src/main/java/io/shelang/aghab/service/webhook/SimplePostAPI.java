package io.shelang.aghab.service.webhook;

import io.shelang.aghab.service.dto.webhook.WebhookAPICallDTO;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

public interface SimplePostAPI {

  @POST
  Response executePost(WebhookAPICallDTO body);
}
