package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.shelang.aghab.domain.Links;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LinksRepository implements PanacheRepository<Links> {
}
