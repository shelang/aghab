package io.shelang.aghab.service.webhook;

import io.shelang.aghab.service.dto.WebhookDTO;
import java.util.List;

public interface WebhookService {

  WebhookDTO getById(Long id);

  List<WebhookDTO> get(String name, Integer page, Integer size);

  WebhookDTO create(WebhookDTO dto);

  WebhookDTO update(Long id, WebhookDTO dto);

  void call(Long webhookId, String hash);

}
