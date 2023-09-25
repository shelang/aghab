package io.shelang.aghab.event.consumer;

import io.quarkus.vertx.ConsumeEvent;
import io.shelang.aghab.domain.LinkAnalytics;
import io.shelang.aghab.event.EventType;
import io.shelang.aghab.event.dto.AnalyticLinkEvent;
import io.shelang.aghab.repository.LinkAnalyticRepository;
import io.shelang.aghab.service.uaa.UserAgentAnalyzer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpHeaders;
import java.time.Instant;
import java.util.Objects;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;

@Slf4j
@ApplicationScoped
public class AnalyticLinkConsumer {

  @Inject
  LinkAnalyticRepository linkAnalyticRepository;

  @ConsumeEvent(value = EventType.ANALYTIC_LINK, blocking = true)
  @Transactional
  public void consume(Message<AnalyticLinkEvent> msg) {
    var linkAnalytics = buildAnalytic(msg.body());
    if (Objects.nonNull(linkAnalytics)) {
      linkAnalyticRepository.persistAndFlush(linkAnalytics);
    }
  }

  private LinkAnalytics buildAnalytic(AnalyticLinkEvent event) {
    if (Objects.isNull(event)) {
      return null;
    }

    String ip = null;

    try {
      ip = event.getHeaders().get("x-forwarded-for").split(",")[0];
      ip = ip.substring(0, 32);
    } catch (Exception ignore) {
      // ignore
    }

    UserAgent.ImmutableUserAgent clientUA =
        UserAgentAnalyzer.parse(event.getHeaders().get(HttpHeaders.USER_AGENT));

    try {
      return LinkAnalytics.builder()
          .linkId(event.getId())
          .ip(ip)
          .os(clientUA.get(UserAgent.OPERATING_SYSTEM_CLASS).getValue())
          .osName(clientUA.get(UserAgent.OPERATING_SYSTEM_NAME).getValue())
          .osVersion(clientUA.get(UserAgent.OPERATING_SYSTEM_VERSION).getValue())
          .device(clientUA.get(UserAgent.DEVICE_CLASS).getValue())
          .deviceName(clientUA.get(UserAgent.DEVICE_NAME).getValue())
          .deviceBrand(clientUA.get(UserAgent.DEVICE_BRAND).getValue())
          .agent(clientUA.get(UserAgent.AGENT_CLASS).getValue())
          .agentName(clientUA.get(UserAgent.AGENT_NAME).getValue())
          .agentVersion(clientUA.get(UserAgent.AGENT_VERSION).getValue())
          .createAt(Instant.now())
          .build();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }
}
