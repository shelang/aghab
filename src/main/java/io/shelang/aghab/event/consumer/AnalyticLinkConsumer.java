package io.shelang.aghab.event.consumer;

import io.quarkus.vertx.ConsumeEvent;
import io.shelang.aghab.domain.LinkAnalytics;
import io.shelang.aghab.event.EventType;
import io.shelang.aghab.event.dto.AnalyticLinkEvent;
import io.shelang.aghab.repository.LinkAnalyticRepository;
import io.shelang.aghab.service.uaa.UserAgentAnalyzerFields;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Objects;

@Slf4j
@ApplicationScoped
public class AnalyticLinkConsumer {

  @Inject LinkAnalyticRepository linkAnalyticRepository;
  @Inject UserAgentAnalyzer uaa;

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
      var parsedUA = uaa.parse(event.getHeaders().get(HttpHeaders.USER_AGENT));

      return LinkAnalytics.builder()
          .linkId(event.getId())
          .os(parsedUA.getValue(UserAgentAnalyzerFields.OperatingSystemClass.field()))
          .osName(parsedUA.getValue(UserAgentAnalyzerFields.OperatingSystemName.field()))
          .osVersion(parsedUA.getValue(UserAgentAnalyzerFields.OperatingSystemVersion.field()))
          .device(parsedUA.getValue(UserAgentAnalyzerFields.DeviceClass.field()))
          .deviceName(parsedUA.getValue(UserAgentAnalyzerFields.AgentName.field()))
          .deviceBrand(parsedUA.getValue(UserAgentAnalyzerFields.DeviceBrand.field()))
          .agent(parsedUA.getValue(UserAgentAnalyzerFields.AgentClass.field()))
          .agentName(parsedUA.getValue(UserAgentAnalyzerFields.AgentName.field()))
          .agentVersion(parsedUA.getValue(UserAgentAnalyzerFields.AgentVersion.field()))
          .createAt(Instant.now())
          .build();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }
}
