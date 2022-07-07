package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.shelang.aghab.domain.LinkAnalytics;
import io.shelang.aghab.enums.AnalyticBucketType;
import io.shelang.aghab.service.dto.AnalyticBucket;
import io.shelang.aghab.service.dto.AnalyticKeyValueDTO;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
public class LinkAnalyticRepository implements PanacheRepository<LinkAnalytics> {

  @SuppressWarnings("unchecked")
  public List<AnalyticBucket> groupByTypeAndLinkIdAndCreateAtBetween(AnalyticBucketType type,
      Long linkId, Instant from, Instant to) {

    Query nativeQuery = getEntityManager().createNativeQuery(
        "SELECT date_trunc(:type, la.create_at) AS \"from\", count(*) \"count\", count(DISTINCT ip) \"uniqCount\" "
            + "FROM link_analytics la "
            + "WHERE la.link_id = :linkId AND la.create_at BETWEEN :from AND :to GROUP BY \"from\"");

    nativeQuery.setParameter("type", type.getType());
    nativeQuery.setParameter("linkId", linkId);
    nativeQuery.setParameter("from", from);
    nativeQuery.setParameter("to", to);

    List<AnalyticBucket> mapped = (List<AnalyticBucket>) nativeQuery.getResultList().stream()
        .map(o -> {
          Object[] rs = (Object[]) o;
          Timestamp f = (Timestamp) rs[0];
          BigInteger count = (BigInteger) rs[1];
          BigInteger uniqCount = (BigInteger) rs[2];
          return new AnalyticBucket().setFrom(f.toInstant()).setCount(count.longValue())
              .setUniqCount(uniqCount.longValue());
        }).collect(Collectors.toList());

    if (!mapped.isEmpty()) {
      int lastIndex = mapped.size() - 1;
      mapped.set(lastIndex, mapped.get(lastIndex).setTo(Instant.now()));
      for (int i = 0; i < lastIndex; i++) {
        mapped.set(i, mapped.get(i).setTo(mapped.get(i + 1).getFrom()));
      }
    }

    return mapped;
  }

  public Pair<Long, Long> countAndUniqCount(Long linkId, Instant from, Instant to) {
    Query nativeQuery = getEntityManager().createNativeQuery(
        "SELECT count(ip) \"count\", count(DISTINCT ip) \"uniqCount\" " + "FROM link_analytics la "
            + "WHERE la.link_id = :linkId AND la.create_at BETWEEN :from AND :to");

    nativeQuery.setParameter("linkId", linkId);
    nativeQuery.setParameter("from", from);
    nativeQuery.setParameter("to", to);

    Object[] rs = (Object[]) nativeQuery.getSingleResult();
    long count = ((BigInteger) rs[0]).longValue();
    long uniqCount = ((BigInteger) rs[1]).longValue();

    return new ImmutablePair<>(count, uniqCount);
  }

  public List<AnalyticKeyValueDTO<String, BigInteger>> top5Devices(Long linkId, Instant from,
      Instant to) {
    var linkIdQuery = Objects.nonNull(linkId) ? "la.link_id = :linkId AND" : "";

    Query nativeQuery = getEntityManager().createNativeQuery(
        "SELECT device, count(device) \"count\"" + "FROM link_analytics la " + "WHERE "
            + linkIdQuery + " la.create_at BETWEEN :from AND :to"
            + " GROUP BY device ORDER BY \"count\" DESC LIMIT 5");

    return listOfAnalyticKeyValueDTO(linkId, from, to, nativeQuery);
  }

  public List<AnalyticKeyValueDTO<String, BigInteger>> top5Oses(Long linkId, Instant from,
      Instant to) {
    var linkIdQuery = Objects.nonNull(linkId) ? "la.link_id = :linkId AND" : "";

    Query nativeQuery = getEntityManager().createNativeQuery(
        "SELECT os, count(os) \"count\"" + "FROM link_analytics la " + "WHERE " + linkIdQuery
            + " la.create_at BETWEEN :from AND :to"
            + " GROUP BY os ORDER BY \"count\" DESC LIMIT 5");

    return listOfAnalyticKeyValueDTO(linkId, from, to, nativeQuery);
  }

  public List<AnalyticKeyValueDTO<String, BigInteger>> top5AgentNames(Long linkId, Instant from,
      Instant to) {
    var linkIdQuery = Objects.nonNull(linkId) ? "la.link_id = :linkId AND" : "";

    Query nativeQuery = getEntityManager().createNativeQuery(
        "SELECT agent_name, count(agent_name) \"count\"" + "FROM link_analytics la " + "WHERE "
            + linkIdQuery + " la.create_at BETWEEN :from AND :to"
            + " GROUP BY agent_name ORDER BY \"count\" DESC LIMIT 5");

    return listOfAnalyticKeyValueDTO(linkId, from, to, nativeQuery);
  }


  public List<AnalyticKeyValueDTO<String, BigInteger>> top5DeviceBrands(Long linkId, Instant from,
      Instant to) {
    var linkIdQuery = Objects.nonNull(linkId) ? "la.link_id = :linkId AND" : "";

    Query nativeQuery = getEntityManager().createNativeQuery(
        "SELECT device_brand, count(device_brand) \"count\"" + "FROM link_analytics la " + "WHERE "
            + linkIdQuery + " la.create_at BETWEEN :from AND :to"
            + " GROUP BY device_brand ORDER BY \"count\" DESC LIMIT 5");

    return listOfAnalyticKeyValueDTO(linkId, from, to, nativeQuery);
  }

  public List<AnalyticKeyValueDTO<String, BigInteger>> top5DeviceNames(Long linkId, Instant from,
      Instant to) {
    var linkIdQuery = Objects.nonNull(linkId) ? "la.link_id = :linkId AND" : "";

    Query nativeQuery = getEntityManager().createNativeQuery(
        "SELECT device_name, count(device_name) \"count\"" + "FROM link_analytics la " + "WHERE "
            + linkIdQuery + " la.create_at BETWEEN :from AND :to"
            + " GROUP BY device_name ORDER BY \"count\" DESC LIMIT 5");

    return listOfAnalyticKeyValueDTO(linkId, from, to, nativeQuery);
  }

  @SuppressWarnings("unchecked")
  public List<AnalyticKeyValueDTO<String, BigInteger>> getLast10UniqIP() {
    Query nativeQuery = getEntityManager().createNativeQuery(
        "SELECT DISTINCT (ip) FROM "
            + "(SELECT ip, la.create_at FROM link_analytics la ORDER BY create_at DESC LIMIT 25) as tmp");

    return (List<AnalyticKeyValueDTO<String, BigInteger>>) nativeQuery.getResultList().stream()
        .map(o -> {
          String key = (String) o;
          return new AnalyticKeyValueDTO<String, Void>().setKey(key);
        }).collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  private List<AnalyticKeyValueDTO<String, BigInteger>> listOfAnalyticKeyValueDTO(Long linkId,
      Instant from, Instant to, Query nativeQuery) {
    if (Objects.nonNull(linkId)) {
      nativeQuery.setParameter("linkId", linkId);
    }
    nativeQuery.setParameter("from", from);
    nativeQuery.setParameter("to", to);

    return (List<AnalyticKeyValueDTO<String, BigInteger>>) nativeQuery.getResultList().stream()
        .map(o -> {
          Object[] rs = (Object[]) o;
          String key = (String) rs[0];
          BigInteger count = (BigInteger) rs[1];
          return new AnalyticKeyValueDTO<String, BigInteger>().setKey(key).setValue(count);
        }).collect(Collectors.toList());
  }
}
