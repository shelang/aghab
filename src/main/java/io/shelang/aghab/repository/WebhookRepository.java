package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.shelang.aghab.domain.Webhook;
import java.util.List;
import java.util.Objects;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WebhookRepository implements PanacheRepository<Webhook> {

  public List<Webhook> search(String name, Long userId, Page page) {
    PanacheQuery<Webhook> query =
        find(
            "SELECT w FROM Webhook w "
                + "LEFT JOIN WebhookUser wu on wu.webhookId = w.id "
                + "WHERE wu.userId = ?1",
            Sort.by("id"), userId);
    if (Objects.nonNull(name) && name.length() > 0) {
      query =
          find(
              "SELECT w FROM Webhook w "
                  + "LEFT JOIN WebhookUser wu on wu.webhookId = w.id "
                  + "WHERE w.name like CONCAT('%', ?1, '%') and wu.userId = ?2",
              Sort.by("id"), name, userId);
    }
    return query.page(page).list();
  }
}
