package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.shelang.aghab.domain.Script;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ScriptRepository implements PanacheRepository<Script> {

  public List<Script> search(String name, Long userId, Page page) {
    Map<String, Object> params = new HashMap<>();
    params.put("userId", userId);

    PanacheQuery<Script> query =
        find(
            "SELECT s FROM io.shelang.aghab.domain.Script s "
                + "LEFT JOIN io.shelang.aghab.domain.ScriptUser su on su.scriptId = s.id "
                + "WHERE su.userId = :userId ",
            Sort.by("s.id"), params);
    if (Objects.nonNull(name)) {
      params.put("name", name);
      query =
          find(
              "SELECT s FROM Script s "
                  + "LEFT JOIN ScriptUser su on su.scriptId = s.id "
                  + "WHERE s.name like CONCAT('%', :name, '%') and su.userId = :userId ",
              Sort.by("s.id"), params);
    }
    return query.page(page).list();
  }
}
