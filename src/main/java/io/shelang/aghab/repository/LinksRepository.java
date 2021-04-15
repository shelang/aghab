package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.shelang.aghab.domain.Links;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class LinksRepository implements PanacheRepository<Links> {
  public Optional<Links> findByHash(String hash) {
    return find("hash", hash).firstResultOptional();
  }
}
