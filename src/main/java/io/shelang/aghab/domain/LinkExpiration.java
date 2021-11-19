package io.shelang.aghab.domain;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
@Entity
@Table(name = "link_expiration")
public class LinkExpiration {

  @Id
  @Column(name = "link_id")
  private Long linkId;

  @Column(name = "expire_at")
  private Instant expireAt;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "link_id")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Link link;
}
