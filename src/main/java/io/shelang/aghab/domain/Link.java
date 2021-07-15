package io.shelang.aghab.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

  @OneToOne(mappedBy = "links", cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "id", referencedColumnName = "link_id")
  @PrimaryKeyJoinColumn()
  private LinkMeta linkMeta;
}
