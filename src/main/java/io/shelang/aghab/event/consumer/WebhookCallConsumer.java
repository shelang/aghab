package io.shelang.aghab.event.consumer;

import io.quarkus.redis.client.RedisClient;
import io.quarkus.vertx.ConsumeEvent;
import io.shelang.aghab.domain.WebhookLink;
import io.shelang.aghab.event.EventType;
import io.shelang.aghab.event.dto.WebhookCallEvent;
import io.shelang.aghab.repository.LinksRepository;
import io.shelang.aghab.repository.WebhookLinkRepository;
import io.shelang.aghab.repository.WebhookRepository;
import io.shelang.aghab.service.webhook.WebhookService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;

@ApplicationScoped
public class WebhookCallConsumer {

  private static final String LOCK_EXPIRE = "100";

  @Inject RedisClient redisClient;
  @Inject LinksRepository linksRepository;
  @Inject WebhookService webhookService;
  @Inject WebhookRepository webhookRepository;
  @Inject WebhookLinkRepository webhookLinkRepository;

  @ConsumeEvent(value = EventType.WEBHOOK_CALL, blocking = true)
  @Transactional
  public void consumer(WebhookCallEvent event) {
    String key = event.getLinkId().toString();
    String setNXResponse = redisClient.setnx(key, String.valueOf(Instant.now())).toString();

    if (!setNXResponse.equals("1")) {
      return;
    }

    redisClient.expire(key, LOCK_EXPIRE);

    webhookService.call(event.getWebhookId(), event.getHash());

    webhookLinkRepository.persistAndFlush(
        new WebhookLink(new WebhookLink.WebhookLinkId(event.getLinkId(), event.getWebhookId()), 1));

    linksRepository.removeWebhookLink(event.getLinkId());
  }
}
