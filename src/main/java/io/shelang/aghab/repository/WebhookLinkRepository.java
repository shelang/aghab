package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.shelang.aghab.domain.WebhookLink;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WebhookLinkRepository
    implements PanacheRepositoryBase<WebhookLink, WebhookLink.WebhookLinkId> {

}
