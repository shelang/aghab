package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.shelang.aghab.domain.LinkUser;
import java.util.List;
import java.util.Objects;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LinkUserRepository implements PanacheRepositoryBase<LinkUser, LinkUser.LinkUserId> {

  public List<LinkUser> page(Long userId, String hash, Page page) {
    PanacheQuery<LinkUser> query;
    if (Objects.nonNull(hash) && !hash.isEmpty()) {
      query =
          find(
              "userId = ?1 and linkHash = ?2 ",
              Sort.by("createAt", Sort.Direction.Descending),
              userId,
              hash);
    } else {
      query = find("userId = ?1", Sort.by("createAt", Sort.Direction.Descending), userId);
    }
    return query.page(page).list();
  }
}
