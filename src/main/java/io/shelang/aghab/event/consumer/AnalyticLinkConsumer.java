package io.shelang.aghab.event.consumer;

import io.quarkus.vertx.ConsumeEvent;
import io.shelang.aghab.domain.LinkAnalytics;
import io.shelang.aghab.event.EventType;
import io.shelang.aghab.repository.LinkAnalyticRepository;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.Instant;

@ApplicationScoped
public class AnalyticLinkConsumer {

  private final Logger logger = LoggerFactory.getLogger(AnalyticLinkConsumer.class);

  private final LinkAnalyticRepository linkAnalyticRepository;

  public AnalyticLinkConsumer(LinkAnalyticRepository linkAnalyticRepository) {
    this.linkAnalyticRepository = linkAnalyticRepository;
  }

  @ConsumeEvent(value = EventType.ANALYTIC_LINK, blocking = true)
  @Transactional
  public void consume(Message<Long> msg) {
    //    System.out.println("hi");
    //    Executors.newCachedThreadPool()
    //        .execute(
    //            () -> {
    //              System.out.println("execed");
    LinkAnalytics linkAnalytics =
        new LinkAnalytics().setLinkId(msg.body()).setCreatedAt(Instant.now());
    linkAnalyticRepository.persistAndFlush(linkAnalytics);
    //            });
  }
}
