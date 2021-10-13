package io.shelang.aghab.service.analytic.impl;

import io.shelang.aghab.enums.AnalyticBucketType;
import io.shelang.aghab.repository.LinkAnalyticRepository;
import io.shelang.aghab.repository.LinksRepository;
import io.shelang.aghab.service.analytic.AnalyticService;
import io.shelang.aghab.service.dto.AnalyticBucket;
import io.shelang.aghab.service.dto.AnalyticDTO;
import io.shelang.aghab.service.dto.AnalyticRequestDTO;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotNull;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@ApplicationScoped
public class AnalyticServiceImpl implements AnalyticService {

  final LinksRepository linksRepository;
  final LinkAnalyticRepository linkAnalyticRepository;

  public AnalyticServiceImpl(
      LinksRepository linksRepository, LinkAnalyticRepository linkAnalyticRepository) {
    this.linksRepository = linksRepository;
    this.linkAnalyticRepository = linkAnalyticRepository;
  }

  private Instant toInstant(Object input, Instant defaultValue) {
    try {
      if (input instanceof String) return Instant.ofEpochMilli(Long.parseLong((String) input));
    } catch (Exception ignore) {
      // ignored
    }
    try {
      if (input instanceof String) return Instant.parse((String) input);
      return defaultValue;
    } catch (Exception ignore) {
      log.error("[Analytics] Can not parse {}", input);
      return defaultValue;
    }
  }

  @Override
  public AnalyticDTO getAndCount(@NotNull Long linkId, @NotNull AnalyticRequestDTO request) {
    var link = linksRepository.findByIdOptional(linkId).orElseThrow(NotFoundException::new);
    Instant f = toInstant(request.getFrom(), link.getLinkMeta().getCreateAt());
    Instant t = toInstant(request.getTo(), Instant.now());

    AtomicLong count = new AtomicLong(0);

    AtomicReference<List<AnalyticBucket>> buckets = new AtomicReference<>(Collections.emptyList());
    AnalyticBucketType.from(request.getBucket())
        .ifPresentOrElse(
            type -> {
              buckets.set(linkAnalyticRepository.groupByTypeAndLinkIdAndCreateAtBetween(type, linkId, f, t));
              count.set(buckets.get().stream().map(AnalyticBucket::getCount).reduce(0L, Long::sum));
            },
            () ->
                count.set(
                    linkAnalyticRepository.count(
                        "link_id = ?1 and (create_at > ?2 and create_at < ?3)",
                        link.getId(),
                        f,
                        t)));

    return new AnalyticDTO()
        .setLinkId(linkId)
        .setCount(count.get())
        .setFrom(f)
        .setTo(t)
        .setBuckets(buckets.get());
  }
}
