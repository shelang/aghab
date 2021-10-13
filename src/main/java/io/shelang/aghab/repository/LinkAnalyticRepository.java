package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.shelang.aghab.domain.LinkAnalytics;
import io.shelang.aghab.enums.AnalyticBucketType;
import io.shelang.aghab.service.dto.AnalyticBucket;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LinkAnalyticRepository implements PanacheRepository<LinkAnalytics> {

  @SuppressWarnings("unchecked")
  public List<AnalyticBucket> groupByTypeAndLinkIdAndCreateAtBetween(
      AnalyticBucketType type, Long linkId, Instant from, Instant to) {

    Query nativeQuery =
        getEntityManager()
            .createNativeQuery(
                "SELECT date_trunc(:type, la.create_at) AS \"from\", count(*) \"count\" "
                    + "FROM link_analytics la "
                    + "WHERE la.link_id = :linkId AND la.create_at BETWEEN :from AND :to GROUP BY \"from\"");

    nativeQuery.setParameter("type", type.getType());
    nativeQuery.setParameter("linkId", linkId);
    nativeQuery.setParameter("from", from);
    nativeQuery.setParameter("to", to);

    List<AnalyticBucket> mapped =
        (List<AnalyticBucket>)
            nativeQuery.getResultList().stream()
                .map(
                    o -> {
                      Object[] rs = (Object[]) o;
                      Timestamp f = (Timestamp) rs[0];
                      BigInteger count = (BigInteger) rs[1];
                      return new AnalyticBucket()
                          .setFrom(f.toInstant())
                          .setCount(count.longValue());
                    })
                .collect(Collectors.toList());

    if (!mapped.isEmpty()) {
      int lastIndex = mapped.size() - 1;
      mapped.set(lastIndex, mapped.get(lastIndex).setTo(Instant.now()));
      for (int i = 0; i < lastIndex - 1; i++) {
        mapped.set(i, mapped.get(i).setTo(mapped.get(i + 1).getFrom()));
      }
    }

    return mapped;
  }
}
