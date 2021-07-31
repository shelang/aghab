package io.shelang.aghab.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "link_analytics")
public class LinkAnalytics {

  @Id
  @Column(name = "link_id")
  private Long linkId;

  private String os; //

  @Column(name = "os_name")
  private String osName; // iOS, Linux, Android, Windows

  @Column(name = "os_version")
  private String osVersion;

  private String device; // Desktop, Phone, Tablet

  @Column(name = "device_name")
  private String deviceName; // Apple iPhone,

  @Column(name = "device_brand")
  private String deviceBrand; // Desktop, Phone, Tablet

  private String agent; // Chrome, Safari, FireFox,

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
