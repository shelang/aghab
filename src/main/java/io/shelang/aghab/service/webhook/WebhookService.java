package io.shelang.aghab.service.webhook;

import io.shelang.aghab.service.webhook.dto.WebhookDTO;

import java.util.List;

public interface WebhookService {

    WebhookDTO getById(Long id);

    List<WebhookDTO> get(String name, Integer page, Integer size);

    WebhookDTO create(WebhookDTO dto);

    WebhookDTO update(WebhookDTO dto);

}
