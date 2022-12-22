package io.shelang.aghab.service.webhook;

import io.shelang.aghab.service.dto.webhook.WebhookDTO;
import java.util.List;

public interface WebhookService {

  WebhookDTO getById(Long id);

  List<WebhookDTO> search(String name, Integer page, Integer size);

  WebhookDTO create(WebhookDTO dto);

  WebhookDTO update(WebhookDTO dto);

  void call(Long webhookId, String hash);

}
