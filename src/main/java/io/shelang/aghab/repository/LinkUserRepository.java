package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.shelang.aghab.domain.LinkUser;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LinkUserRepository implements PanacheRepositoryBase<LinkUser, LinkUser.LinkUserId> {}
