package io.shelang.aghab.service.mapper;

import io.shelang.aghab.domain.Webhook;
import io.shelang.aghab.service.dto.WebhookDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface WebhookMapper extends EntityMapper<WebhookDTO, Webhook> {}
