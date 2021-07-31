package io.shelang.aghab.service.uaa;

import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@Slf4j
@ApplicationScoped
public class UserAgent {

  private static final String[] FIELDS = {
    UserAgentAnalyzerFields.OperatingSystemClass.field(),
    UserAgentAnalyzerFields.OperatingSystemName.field(),
    UserAgentAnalyzerFields.OperatingSystemVersion.field(),
    UserAgentAnalyzerFields.AgentClass.field(),
    UserAgentAnalyzerFields.AgentName.field(),
    UserAgentAnalyzerFields.AgentVersion.field(),
    UserAgentAnalyzerFields.DeviceClass.field(),
    UserAgentAnalyzerFields.DeviceBrand.field(),
    UserAgentAnalyzerFields.DeviceName.field(),
  };

  @Produces
  @Singleton
  UserAgentAnalyzer userAgentAnalyzer() {
    log.info("Build User Agent Analyzer");
    return UserAgentAnalyzer
            .newBuilder()
            .hideMatcherLoadStats()
            .withFields(FIELDS)
            .withCache(10000)
            .build();
  }
}
