package io.shelang.aghab.service.analytic.impl;

import io.shelang.aghab.repository.LinkAnalyticRepository;
import io.shelang.aghab.repository.LinksRepository;
import io.shelang.aghab.service.analytic.AnalyticService;
import io.shelang.aghab.service.dto.AnalyticDTO;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;
import java.time.Instant;

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
      if (input instanceof String)
        return Instant.ofEpochMilli(Long.parseLong((String) input));
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
  public AnalyticDTO getAndCount(Long linkId, String from, String to) {
    var link = linksRepository.findByIdOptional(linkId).orElseThrow(NotFoundException::new);
    Instant f = toInstant(from, link.getLinkMeta().getCreateAt());
    Instant t = toInstant(to, Instant.now());

    long count =
        linkAnalyticRepository.count(
            "link_id = ?1 and (create_at > ?2 and create_at < ?3)", link.getId(), f, t);
    return new AnalyticDTO().setLinkId(linkId).setCount(count).setFrom(f).setTo(t);
  }
}
