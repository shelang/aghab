package io.shelang.aghab.domain;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
@Entity
@Table(name = "link_meta")
public class LinkMeta {

  @Id
  @GeneratedValue(generator = "link_meta_id_gen", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "link_meta_id_gen",
      allocationSize = 1,
      sequenceName = "link_meta_id_seq")
  private Long id;

  private String title;
  private String description;

  @CreationTimestamp
  @Column(name = "create_at")
  private Instant createAt;

  @UpdateTimestamp
  @Column(name = "update_at")
  private Instant updateAt;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "link_id")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Link link;
}
