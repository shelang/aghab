package io.shelang.aghab.service.webhook;

import io.quarkus.panache.common.Page;
import io.shelang.aghab.domain.Webhook;
import io.shelang.aghab.domain.WebhookUser;
import io.shelang.aghab.repository.WebhookRepository;
import io.shelang.aghab.repository.WebhookUserRepository;
import io.shelang.aghab.service.mapper.WebhookMapper;
import io.shelang.aghab.service.webhook.dto.WebhookDTO;
import io.shelang.aghab.util.NumberUtil;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WebhookServiceImpl implements WebhookService {

  @Inject WebhookRepository webhookRepository;
  @Inject WebhookUserRepository webhookUserRepository;
  @Inject WebhookMapper webhookMapper;

  @Inject
  @Claim(standard = Claims.sub)
  Long userId;

  @Override
  public WebhookDTO getById(Long id) {
    return webhookMapper.toDTO(getValidatedWebhook(id));
  }

  @Override
  public List<WebhookDTO> get(String name, Integer page, Integer size) {
    page = NumberUtil.normalizeValue(page, 1) - 1;
    size = NumberUtil.normalizeValue(size, 10);

    if (size > 50) size = 50;

    return webhookMapper.toDTO(webhookRepository.search(name, userId, Page.of(page, size)));
  }

  @Override
  @Transactional
  public WebhookDTO create(WebhookDTO dto) {
    Webhook webhook = webhookMapper.toEntity(dto);
    webhookRepository.persistAndFlush(webhook);
    saveWebhookUser(webhook);
    return webhookMapper.toDTO(webhook);
  }

  @Override
  @Transactional
  public WebhookDTO update(WebhookDTO dto) {
    Webhook webhook = getValidatedWebhook(dto.getId());
    webhook.setUrl(dto.getUrl());
    webhookRepository.persistAndFlush(webhook);
    saveWebhookUser(webhook);
    return webhookMapper.toDTO(webhook);
  }

  private Webhook getValidatedWebhook(Long id) {
    WebhookUser webhookUser = getWebhookUser(id).orElseThrow(ForbiddenException::new);
    Webhook webhook = webhookRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    if (!userId.equals(webhookUser.getUserId())) throw new ForbiddenException();
    return webhook;
  }

  private Optional<WebhookUser> getWebhookUser(Long id) {
    return webhookUserRepository.findByIdOptional(makeWebhookUser(id).getId());
  }

  @Transactional
  private void saveWebhookUser(Webhook webhook) {
    WebhookUser webhookUser = makeWebhookUser(webhook.getId());
    Optional<WebhookUser> exist = webhookUserRepository.findByIdOptional(webhookUser.getId());
    if (exist.isEmpty()) webhookUserRepository.persistAndFlush(webhookUser);
  }

  private WebhookUser makeWebhookUser(Long id) {
    return new WebhookUser().setWebhookId(id).setUserId(userId).setId(getWebhookUserId(id));
  }

  private WebhookUser.WebhookUserId getWebhookUserId(Long id) {
    return new WebhookUser.WebhookUserId(userId, id);
  }
}
