package io.shelang.aghab.domain;

import io.shelang.aghab.domain.enums.PostgreSQLEnumType;
import io.shelang.aghab.enums.LinkStatus;
import io.shelang.aghab.enums.RedirectType;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
@Entity
@Table(name = "links")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class Link {

  @Id
  @GeneratedValue(generator = "links_id_gen", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "links_id_gen", allocationSize = 1, sequenceName = "links_id_seq")
  private Long id;

  private String hash;
  private String alias;
  private String url;

  @Enumerated(EnumType.ORDINAL)
  private LinkStatus status;

  @Column(name = "redirect_code")
  @Builder.Default
  private short redirectCode = 301;

  @Column(name = "forward_parameter")
  private boolean forwardParameter;

  @OneToOne(mappedBy = "link", cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "id", referencedColumnName = "link_id")
  @PrimaryKeyJoinColumn()
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private LinkMeta linkMeta;

  @OneToOne(mappedBy = "link", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "id", referencedColumnName = "link_id")
  @PrimaryKeyJoinColumn()
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private LinkExpiration linkExpiration;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "link_id")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<LinkAlternative> alternatives;

  @Enumerated(EnumType.STRING)
  @Type(type = "pgsql_enum")
  @Column(columnDefinition = "redirect_type")
  private RedirectType type;

  @Column(name = "script_id")
  private Long scriptId;

  @Column(name = "webhook_id")
  private Long webhookId;
}
