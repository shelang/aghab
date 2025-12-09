package io.shelang.aghab.domain;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "link_analytics")
public class LinkAnalytics {

  @Id
  @org.hibernate.annotations.UuidGenerator
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "link_id")
  private Long linkId;

  private String os; // mobile, desktop, tablet

  @Column(name = "os_name")
  private String osName; // iOS, Linux, Android, Windows

  @Column(name = "os_version")
  private String osVersion;

  private String device; // Desktop, Phone, Tablet, ...

  @Column(name = "device_name")
  private String deviceName;

  @Column(name = "device_brand")
  private String deviceBrand;

  private String agent; // Chrome, Safari, FireFox, Postman, ...

  @Column(name = "agent_name")
  private String agentName;

  @Column(name = "agent_version")
  private String agentVersion;

  private String location;
  private String ip;

  @CreationTimestamp
  @Column(name = "create_at")
  private Instant createAt;
}
