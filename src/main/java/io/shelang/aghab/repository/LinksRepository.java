package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.shelang.aghab.domain.Link;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class LinksRepository implements PanacheRepository<Link> {
  public Optional<Link> findByHash(String hash) {
    return find("hash", hash).firstResultOptional();
  }

  public boolean removeWebhookLink(Long id) {
    return update("webhook_id = NULL WHERE id = ?1", id) > 0;
  }
}
