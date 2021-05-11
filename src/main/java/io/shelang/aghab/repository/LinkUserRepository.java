package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import io.shelang.aghab.domain.LinkUser;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class LinkUserRepository implements PanacheRepositoryBase<LinkUser, LinkUser.LinkUserId> {
  public List<LinkUser> page(Long userId, Integer page, Integer size) {
    return find("user_id = ?1", Sort.by("create_at", Sort.Direction.Descending), userId)
        .page(page, size)
        .list();
  }
}
