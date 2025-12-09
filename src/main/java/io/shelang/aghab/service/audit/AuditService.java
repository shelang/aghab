package io.shelang.aghab.service.audit;

import io.shelang.aghab.domain.AuditLog;
import io.shelang.aghab.repository.AuditLogRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AuditService {

    @Inject
    AuditLogRepository auditLogRepository;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void log(Long userId, Long workspaceId, String eventType, String eventData, String ipAddress) {
        try {
            AuditLog log = AuditLog.builder()
                    .userId(userId)
                    .workspaceId(workspaceId)
                    .eventType(eventType)
                    .eventData(eventData)
                    .ipAddress(ipAddress)
                    .build();
            auditLogRepository.persistAndFlush(log);
        } catch (Exception e) {
            // Fail silently to not impact main business logic
        }
    }
}
