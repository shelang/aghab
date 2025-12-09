package io.shelang.aghab.event.consumer;

import io.quarkus.vertx.ConsumeEvent;
import io.shelang.aghab.domain.WebhookLink;
import io.shelang.aghab.event.EventType;
import io.shelang.aghab.event.dto.WebhookCallEvent;
import io.shelang.aghab.repository.LinksRepository;
import io.shelang.aghab.repository.WebhookLinkRepository;
import io.shelang.aghab.service.webhook.WebhookService;
import java.time.Instant;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class WebhookCallConsumer {

  private static final long LOCK_EXPIRE = 100;

  @Inject
  io.quarkus.redis.datasource.RedisDataSource redisDataSource;
  @Inject
  LinksRepository linksRepository;
  @Inject
  WebhookService webhookService;
  @Inject
  WebhookLinkRepository webhookLinkRepository;

  @ConsumeEvent(value = EventType.WEBHOOK_CALL, blocking = true)
  @Transactional
  public void consumer(WebhookCallEvent event) {
    String key = event.getLinkId().toString();
    boolean setNXResponse = redisDataSource.value(String.class).setnx(key, String.valueOf(Instant.now()));

    if (!setNXResponse) {
      return;
    }

    redisDataSource.key().expire(key, LOCK_EXPIRE);

    webhookService.call(event.getWebhookId(), event.getLinkId(), event.getHash());

    webhookLinkRepository.persistAndFlush(
        new WebhookLink(new WebhookLink.WebhookLinkId(event.getLinkId(), event.getWebhookId()),
            1L));

    linksRepository.removeWebhookLink(event.getLinkId());
  }
}
