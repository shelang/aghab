package io.shelang.aghab.domain;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
