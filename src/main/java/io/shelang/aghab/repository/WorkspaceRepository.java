package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.shelang.aghab.domain.Workspace;
import java.util.List;
import java.util.Objects;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkspaceRepository implements PanacheRepository<Workspace> {

  public List<Workspace> search(String q, Long userId, Page page) {
    PanacheQuery<Workspace> query =
        find(
            "SELECT w FROM Workspace w "
                + "LEFT JOIN WorkspaceUser wu on wu.workspaceId = w.id "
                + "WHERE wu.userId = ?1",
            Sort.by("id"), userId);
    if (Objects.nonNull(q) && q.length() > 0) {
      query =
          find(
              "SELECT w FROM Workspace w "
                  + "LEFT JOIN WorkspaceUser wu on wu.workspaceId = w.id "
                  + "WHERE w.name like CONCAT('%', ?1, '%') and wu.userId = ?2",
              Sort.by("id"), q, userId);
    }
    return query.page(page).list();
  }

}
