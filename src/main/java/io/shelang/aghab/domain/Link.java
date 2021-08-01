package io.shelang.aghab.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "links")
public class Link {

  @Id
  @GeneratedValue(generator = "links_id_gen", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "links_id_gen", allocationSize = 1, sequenceName = "links_id_seq")
  private Long id;

  private String hash;
  private String alias;
  private String url;
  private Integer status;

  @Column(name = "redirect_code")
  @Builder.Default
  private short redirectCode = 301;

  @Column(name = "forward_parameter")
  private boolean forwardParameter;

  @OneToOne(mappedBy = "links", cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "id", referencedColumnName = "link_id")
  @PrimaryKeyJoinColumn()
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private LinkMeta linkMeta;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "link_id")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<LinkAlternative> alternatives;
}
