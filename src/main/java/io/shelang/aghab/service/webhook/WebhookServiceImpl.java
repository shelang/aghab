package io.shelang.aghab.service.webhook;

import io.shelang.aghab.domain.Webhook;
import io.shelang.aghab.domain.WebhookUser;
import io.shelang.aghab.repository.WebhookRepository;
import io.shelang.aghab.repository.WebhookUserRepository;
import io.shelang.aghab.service.dto.webhook.WebhookAPICallDTO;
import io.shelang.aghab.service.dto.webhook.WebhookDTO;
import io.shelang.aghab.service.mapper.WebhookMapper;
import io.shelang.aghab.service.user.TokenService;
import io.shelang.aghab.util.PageUtil;
import io.shelang.aghab.util.StringUtil;
import io.shelang.aghab.util.UrlValidator;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import io.shelang.aghab.service.dto.webhook.WebhookAPICallDTO;
import java.net.URI;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;

@ApplicationScoped
@Slf4j
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
    return webhookMapper.toDTO(getWebhook(id));
  }

  @Override
  public WebhookDTO getByIdAndValidate(Long id) {
    return webhookMapper.toDTO(getValidatedWebhook(id));
  }

  @Override
  public List<WebhookDTO> search(String name, Integer page, Integer size) {
    return webhookMapper.toDTO(
        webhookRepository.search(name, tokenService.getAccessTokenUserId(),
            PageUtil.of(page, size)));
  }

  @Override
  @Transactional
  public WebhookDTO create(WebhookDTO dto) {
    UrlValidator.validateOrThrow(dto.getUrl());
    Webhook webhook = webhookMapper.toEntity(dto);
    webhook.setId(null);
    webhookRepository.persistAndFlush(webhook);
    saveWebhookUser(webhook);
    return webhookMapper.toDTO(webhook);
  }

  @Override
  @Transactional
  public WebhookDTO update(WebhookDTO dto) {
    Webhook webhook = getValidatedWebhook(dto.getId());
    if (StringUtil.nonNullOrEmpty(dto.getUrl())) {
      UrlValidator.validateOrThrow(dto.getUrl());
      webhook.setUrl(dto.getUrl());
    }
    if (StringUtil.nonNullOrEmpty(dto.getName())) {
      webhook.setName(dto.getName());
    }
    webhookRepository.persistAndFlush(webhook);
    saveWebhookUser(webhook);
    return webhookMapper.toDTO(webhook);
  }

  @Override
  @Retry(maxRetries = 3, delay = 200)
  @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 1000)
  public void call(Long webhookId, Long linkId, String hash) {
    webhookRepository
        .findByIdOptional(webhookId)
        .ifPresent(
            webhook -> {
              try {
                SimplePostAPI api = RestClientBuilder.newBuilder()
                    .baseUri(new URI(webhook.getUrl()))
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .build(SimplePostAPI.class);
                WebhookAPICallDTO dto = new WebhookAPICallDTO()
                    .setLinkId(linkId)
                    .setHash(hash)
                    .setDate(Instant.now());
                try (Response response = api.executePost(dto)) {
                  if (response.getStatus() < 200 || response.getStatus() >= 300) {
                    throw new RuntimeException("Call failed with status " + response.getStatus());
                  }
                }
              } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException("Webhook call failed", e);
              }
            });
  }

  private Webhook getWebhook(Long id) {
    return webhookRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
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
