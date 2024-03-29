package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.shelang.aghab.domain.LinkExpiration;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LinkExpirationRepository implements PanacheRepository<LinkExpiration> {

}
