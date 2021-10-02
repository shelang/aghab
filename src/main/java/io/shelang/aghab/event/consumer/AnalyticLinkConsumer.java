package io.shelang.aghab.event.consumer;

import io.quarkus.vertx.ConsumeEvent;
import io.shelang.aghab.domain.LinkAnalytics;
import io.shelang.aghab.event.EventType;
import io.shelang.aghab.event.dto.AnalyticLinkEvent;
import io.shelang.aghab.repository.LinkAnalyticRepository;
import io.shelang.aghab.service.uaa.UserAgentAnalyzer;
import io.vertx.core.eventbus.Message;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Slf4j
@ApplicationScoped
public class AnalyticLinkConsumer {

  @Inject LinkAnalyticRepository linkAnalyticRepository;

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
    try {

      log.error("headers: {}", event.getHeaders());

      String ip = null;
      String os = null;
      String device = null;
      try {
        ip = event.getHeaders().get("x-forwarded-for").split(",")[0];
        ip = ip.substring(0, 32);
      } catch (Exception ignore) {
        // igonre
      }

      try {
        List<String> uaa = UserAgentAnalyzer.detectType(event.getHeaders().get("user-agent"));
        device = !uaa.isEmpty() ? uaa.get(0) : null;
        os = uaa.size() > 1 ? uaa.get(1) : null;
      } catch (Exception e) {
        // ignore
      }

      return LinkAnalytics.builder()
          .linkId(event.getId())
          .ip(ip)
          .os(os)
          .device(device)
          .createAt(Instant.now())
          .build();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }
}
