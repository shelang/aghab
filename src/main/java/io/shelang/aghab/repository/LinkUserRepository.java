package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import io.shelang.aghab.domain.LinkUser;
import java.util.List;
import java.util.Objects;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LinkUserRepository implements PanacheRepositoryBase<LinkUser, LinkUser.LinkUserId> {

  public List<LinkUser> page(Long userId, String hash, Integer page, Integer size) {
    PanacheQuery<LinkUser> query;
    if (Objects.nonNull(hash) && !hash.isEmpty()) {
      query =
          find(
              "user_id = ?1 and link_hash = ?2 ",
              Sort.by("create_at", Sort.Direction.Descending),
              userId,
              hash);
    } else {
      query = find("user_id = ?1", Sort.by("create_at", Sort.Direction.Descending), userId);
    }
    return query.page(page, size).list();
  }
}
