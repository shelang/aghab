package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.shelang.aghab.domain.Link;
import java.util.Optional;

import io.shelang.aghab.enums.WebhookStatus;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LinksRepository implements PanacheRepository<Link> {

  public Optional<Link> findByHash(String hash) {
    return find("hash", hash).firstResultOptional();
  }

  public void removeWebhookLink(Long id) {
    update("webhookStatus = ?1 WHERE id = ?2", WebhookStatus.SENT, id);
  }
}
