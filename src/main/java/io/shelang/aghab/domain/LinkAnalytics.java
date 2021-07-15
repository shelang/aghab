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

  private String platform;
  private String device;
  private String browser;
  private String location;
  private String ip;

  @CreationTimestamp
  @Column(name = "create_at")
  private Instant createAt;
}
