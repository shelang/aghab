package io.shelang.aghab.service.webhook;

import io.shelang.aghab.domain.Webhook;
import io.shelang.aghab.domain.WebhookUser;
import io.shelang.aghab.repository.WebhookRepository;
import io.shelang.aghab.repository.WebhookUserRepository;
import io.shelang.aghab.service.dto.WebhookAPICallDTO;
import io.shelang.aghab.service.dto.WebhookDTO;
import io.shelang.aghab.service.mapper.WebhookMapper;
import io.shelang.aghab.service.user.TokenService;
import io.shelang.aghab.util.PageUtil;
import io.shelang.aghab.util.StringUtil;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

@ApplicationScoped
public class WebhookServiceImpl implements WebhookService {

  @Inject
  WebhookRepository webhookRepository;
  @Inject
  WebhookUserRepository webhookUserRepository;
  @Inject
  WebhookMapper webhookMapper;
  @Inject
  TokenService tokenService;

  @Override
  public WebhookDTO getById(Long id) {
    return webhookMapper.toDTO(getValidatedWebhook(id));
  }

  @Override
  public List<WebhookDTO> get(String name, Integer page, Integer size) {
    return webhookMapper.toDTO(
        webhookRepository.search(name, tokenService.getAccessTokenUserId(),
            PageUtil.of(page, size)));
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
  public WebhookDTO update(Long id, WebhookDTO dto) {
    Webhook webhook = getValidatedWebhook(id);
    if (StringUtil.isNullOrEmpty(dto.getUrl())) {
      webhook.setUrl(dto.getUrl());
    }
    if (StringUtil.isNullOrEmpty(dto.getName())) {
      webhook.setName(dto.getName());
    }
    webhookRepository.persistAndFlush(webhook);
    saveWebhookUser(webhook);
    return webhookMapper.toDTO(webhook);
  }

  @Override
  public void call(Long webhookId, String hash) {
    webhookRepository
        .findByIdOptional(webhookId)
        .ifPresent(
            webhook -> {
              try {
                SimplePostAPI api =
                    RestClientBuilder.newBuilder()
                        .baseUri(new URI(webhook.getUrl()))
                        .build(SimplePostAPI.class);
                WebhookAPICallDTO dto =
                    new WebhookAPICallDTO().setHash(hash).setDate(Instant.now());
                //noinspection EmptyTryBlock
                try (Response ignored = api.executePost(dto)) {
                  // empty block
                }
              } catch (Exception e) {
                e.printStackTrace();
              }
            });
  }

  private Webhook getValidatedWebhook(Long id) {
    WebhookUser webhookUser = getWebhookUser(id).orElseThrow(ForbiddenException::new);
    Webhook webhook = webhookRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    if (!tokenService.getAccessTokenUserId().equals(webhookUser.getUserId())) {
      throw new ForbiddenException();
    }
    return webhook;
  }

  private Optional<WebhookUser> getWebhookUser(Long id) {
    return webhookUserRepository.findByIdOptional(makeWebhookUser(id).getId());
  }

  @Transactional
  private void saveWebhookUser(Webhook webhook) {
    WebhookUser webhookUser = makeWebhookUser(webhook.getId());
    Optional<WebhookUser> exist = webhookUserRepository.findByIdOptional(webhookUser.getId());
    if (exist.isEmpty()) {
      webhookUserRepository.persistAndFlush(webhookUser);
    }
  }

  private WebhookUser makeWebhookUser(Long id) {
    return new WebhookUser()
        .setWebhookId(id)
        .setUserId(tokenService.getAccessTokenUserId())
        .setId(getWebhookUserId(id));
  }

  private WebhookUser.WebhookUserId getWebhookUserId(Long id) {
    return new WebhookUser.WebhookUserId(tokenService.getAccessTokenUserId(), id);
  }
}
