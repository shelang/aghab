package io.shelang.aghab.service.analytic.impl;

import io.shelang.aghab.domain.Links;
import io.shelang.aghab.repository.LinkAnalyticRepository;
import io.shelang.aghab.repository.LinksRepository;
import io.shelang.aghab.service.analytic.AnalyticService;
import io.shelang.aghab.service.dto.AnalyticDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@ApplicationScoped
public class AnalyticServiceImpl implements AnalyticService {

  final LinksRepository linksRepository;
  final LinkAnalyticRepository linkAnalyticRepository;

  public AnalyticServiceImpl(
      LinksRepository linksRepository, LinkAnalyticRepository linkAnalyticRepository) {
    this.linksRepository = linksRepository;
    this.linkAnalyticRepository = linkAnalyticRepository;
  }

  @Override
  public AnalyticDTO getAndCount(Long linkId, Date from, Date to) {
    Links link = linksRepository.findByIdOptional(linkId).orElseThrow(NotFoundException::new);
    Instant f = Objects.nonNull(from) ? from.toInstant() : link.getLinkMeta().getCreateAt();
    Instant t = Objects.nonNull(from) ? from.toInstant() : Instant.now();

    long count =
        linkAnalyticRepository.count(
            "link_id = ?1 and (create_at > ?2 and create_at < ?3)", link.getId(), f, t);
    return new AnalyticDTO().setLinkId(linkId).setCount(count).setFrom(f).setTo(t);
  }
}
