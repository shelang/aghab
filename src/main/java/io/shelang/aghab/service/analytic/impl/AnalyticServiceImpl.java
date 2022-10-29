package io.shelang.aghab.service.analytic.impl;

import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import io.shelang.aghab.domain.Link;
import io.shelang.aghab.enums.AnalyticBucketType;
import io.shelang.aghab.repository.LinkAnalyticRepository;
import io.shelang.aghab.repository.LinksRepository;
import io.shelang.aghab.service.analytic.AnalyticService;
import io.shelang.aghab.service.dto.AnalyticBucket;
import io.shelang.aghab.service.dto.AnalyticDTO;
import io.shelang.aghab.service.dto.AnalyticKeyValueDTO;
import io.shelang.aghab.service.dto.AnalyticListDTO;
import io.shelang.aghab.service.dto.AnalyticRequestDTO;
import io.shelang.aghab.service.dto.AnalyticTimeRangeRequestDTO;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
@ApplicationScoped
public class AnalyticServiceImpl implements AnalyticService {

  final LinksRepository linksRepository;
  final LinkAnalyticRepository linkAnalyticRepository;

  public AnalyticServiceImpl(LinksRepository linksRepository,
      LinkAnalyticRepository linkAnalyticRepository) {
    this.linksRepository = linksRepository;
    this.linkAnalyticRepository = linkAnalyticRepository;
  }

  private Instant toInstant(Object input, Instant defaultValue) {
    try {
      if (input instanceof String) {
        return Instant.ofEpochMilli(Long.parseLong((String) input));
      }
    } catch (Exception ignore) {
      // ignored
    }
    try {
      if (input instanceof String) {
        return Instant.parse((String) input);
      }
      return defaultValue;
    } catch (Exception ignore) {
      log.error("[Analytics] Can not parse {}", input);
      return defaultValue;
    }
  }

  private Link getLink(Long linkId) {
    Link link;
    if (Objects.nonNull(linkId)) {
      link = linksRepository.findByIdOptional(linkId).orElseThrow(NotFoundException::new);
    } else {
      link = linksRepository.find("", Sort.by("id", Direction.Ascending))
          .page(0, 1)
          .stream().findFirst().orElseThrow(NotFoundException::new);
    }
    return link;
  }

  @Override
  public AnalyticDTO getAndCount(Long linkId, AnalyticRequestDTO request) {
    var link = linksRepository.findByIdOptional(linkId).orElseThrow(NotFoundException::new);
    Instant f = toInstant(request.getFrom(), link.getLinkMeta().getCreateAt());
    Instant t = toInstant(request.getTo(), Instant.now());

    AtomicReference<List<AnalyticBucket>> buckets = new AtomicReference<>(Collections.emptyList());
    Pair<Long, Long> countAndUniqCountPair = linkAnalyticRepository.countAndUniqCount(link.getId(),
        f, t);
    AnalyticBucketType.from(request.getBucket()).ifPresent(type -> buckets.set(
        linkAnalyticRepository.groupByTypeAndLinkIdAndCreateAtBetween(type, linkId, f, t)));

    return new AnalyticDTO().setLinkId(linkId).setCount(countAndUniqCountPair.getLeft())
        .setUniqCount(countAndUniqCountPair.getRight()).setFrom(f).setTo(t)
        .setBuckets(buckets.get());
  }

  @Override
  public AnalyticListDTO top5Devices(Long linkId, AnalyticTimeRangeRequestDTO request) {
    var link = getLink(linkId);
    Instant f = toInstant(request.getFrom(), link.getLinkMeta().getCreateAt());
    Instant t = toInstant(request.getTo(), Instant.now());

    List<AnalyticKeyValueDTO<String, BigInteger>> top5Devices = linkAnalyticRepository.top5Devices(
        linkId, f, t);

    return new AnalyticListDTO().setData(top5Devices);
  }

  @Override
  public AnalyticListDTO top5Oses(Long linkId, AnalyticTimeRangeRequestDTO request) {
    Link link = getLink(linkId);
    Instant f = toInstant(request.getFrom(), link.getLinkMeta().getCreateAt());
    Instant t = toInstant(request.getTo(), Instant.now());

    List<AnalyticKeyValueDTO<String, BigInteger>> top5Oses = linkAnalyticRepository.top5Oses(
        linkId, f, t);

    return new AnalyticListDTO().setData(top5Oses);
  }

  @Override
  public AnalyticListDTO top5AgentNames(Long linkId, AnalyticTimeRangeRequestDTO request) {
    Link link = getLink(linkId);
    Instant f = toInstant(request.getFrom(), link.getLinkMeta().getCreateAt());
    Instant t = toInstant(request.getTo(), Instant.now());

    List<AnalyticKeyValueDTO<String, BigInteger>> top5AgentNames = linkAnalyticRepository.top5AgentNames(
        linkId, f, t);

    return new AnalyticListDTO().setData(top5AgentNames);
  }

  @Override
  public AnalyticListDTO top5DeviceBrands(Long linkId, AnalyticTimeRangeRequestDTO request) {
    Link link = getLink(linkId);
    Instant f = toInstant(request.getFrom(), link.getLinkMeta().getCreateAt());
    Instant t = toInstant(request.getTo(), Instant.now());

    List<AnalyticKeyValueDTO<String, BigInteger>> top5DeviceBrands = linkAnalyticRepository.top5DeviceBrands(
        linkId, f, t);

    return new AnalyticListDTO().setData(top5DeviceBrands);
  }

  @Override
  public AnalyticListDTO top5DeviceNames(Long linkId, AnalyticTimeRangeRequestDTO request) {
    Link link = getLink(linkId);
    Instant f = toInstant(request.getFrom(), link.getLinkMeta().getCreateAt());
    Instant t = toInstant(request.getTo(), Instant.now());

    List<AnalyticKeyValueDTO<String, BigInteger>> top5DeviceNames = linkAnalyticRepository.top5DeviceNames(
        linkId, f, t);

    return new AnalyticListDTO().setData(top5DeviceNames);
  }

  @Override
  public AnalyticListDTO top5OsVersion(Long linkId, AnalyticTimeRangeRequestDTO request) {
    return new AnalyticListDTO();
  }

  @Override
  public AnalyticListDTO getLast10UniqIP(AnalyticTimeRangeRequestDTO request) {
    List<AnalyticKeyValueDTO<String, BigInteger>> getLast10UniqIP = linkAnalyticRepository.getLast10UniqIP();

    return new AnalyticListDTO().setData(getLast10UniqIP);
  }
}
