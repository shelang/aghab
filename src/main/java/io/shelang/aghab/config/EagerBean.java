package io.shelang.aghab.config;

import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;


/**
 * Every Bean need to be initialized eagerly can be injected here
 * You may have a Object that have high load time like UserAgentAnalyzer
 * which may take 2-3 seconds to be fully loaded
 */
@Slf4j
@ApplicationScoped
public class EagerBean {

  @Inject UserAgentAnalyzer uaa;

  void onStart(@Observes StartupEvent ev) {
    log.info("The application is starting...");
  }
}
