package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.shelang.aghab.domain.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

  public Optional<User> findByUsername(String username) {
    return find("username", username).firstResultOptional();
  }

  public List<User> search(String username, Page page) {
    Map<String, Object> params = new HashMap<>();

    PanacheQuery<User> query = find("SELECT u FROM io.shelang.aghab.domain.User u", params);
    if (Objects.nonNull(username) && !username.isEmpty()) {
      query =
          find(
              "SELECT u FROM io.shelang.aghab.domain.User u WHERE u.username = :username ", params);
    }

    return query.page(page).list();
  }
}
