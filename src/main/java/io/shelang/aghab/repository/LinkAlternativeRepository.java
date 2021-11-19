package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.shelang.aghab.domain.LinkAlternative;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LinkAlternativeRepository
    implements PanacheRepositoryBase<LinkAlternative, LinkAlternative.LinkAlternativeId> {}
