package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.shelang.aghab.domain.LinkAnalytics;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LinkAnalyticRepository implements PanacheRepository<LinkAnalytics> {

}
