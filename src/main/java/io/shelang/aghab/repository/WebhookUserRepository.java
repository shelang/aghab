package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.shelang.aghab.domain.WebhookUser;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WebhookUserRepository implements PanacheRepositoryBase<WebhookUser, WebhookUser.WebhookUserId> {}
