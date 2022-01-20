package io.shelang.aghab.job;

import io.quarkus.scheduler.Scheduled;
import io.shelang.aghab.domain.LinkExpiration;
import io.shelang.aghab.enums.LinkStatus;
import io.shelang.aghab.repository.LinkExpirationRepository;
import io.shelang.aghab.repository.LinksRepository;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApplicationScoped
public class ExpireLinkJob {

  private final LinkExpirationRepository linkExpirationRepository;
  private final LinksRepository linkRepository;

  @Inject
  public ExpireLinkJob(
      LinkExpirationRepository linkExpirationRepository, LinksRepository linkRepository) {
    this.linkExpirationRepository = linkExpirationRepository;
    this.linkRepository = linkRepository;
  }

  @Transactional
  @Scheduled(
      cron = "{job.expire.link.cron.expr}",
      identity = "expire-link-job",
      concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
  public void expireLinks() {
    var hasData = true;
    while (hasData) {
      List<LinkExpiration> expired =
          linkExpirationRepository.find("expireAt < now()").page(0, 20).list();
      if (!expired.isEmpty()) log.info("{} expired links fetched", expired.size());
      var linkIds = new ArrayList<>();
      for (LinkExpiration linkExpiration : expired) {
        linkIds.add(linkExpiration.getLinkId());
      }
      if (linkIds.isEmpty()) {
        hasData = false;
      } else {
        linkRepository.update(
            "status = " + LinkStatus.EXPIRED.ordinal() + " where id in (?1)", linkIds);
        linkExpirationRepository.delete("link_id in (?1)", linkIds);
      }
    }
  }
}
