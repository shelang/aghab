package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.shelang.aghab.domain.AuditLog;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuditLogRepository implements PanacheRepository<AuditLog> {
}
