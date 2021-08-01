package io.shelang.aghab.domain;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "link_alternatives")
public class LinkAlternative {

  @Id
  @GeneratedValue(generator = "link_alternatives_id_gen", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "link_alternatives_id_gen",
      allocationSize = 1,
      sequenceName = "link_alternatives_id_seq")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private Link link;

  @Column(length = 50)
  private String key;

  private String url;
}
