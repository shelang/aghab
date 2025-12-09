package io.shelang.aghab.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "workspace_id")
    Long workspaceId;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "event_data")
    private String eventData;

    @Column(name = "ip_address")
    private String ipAddress;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private Instant createAt;
}
