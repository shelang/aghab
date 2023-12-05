package io.shelang.aghab.domain;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import io.shelang.aghab.enums.LinkStatus;
import io.shelang.aghab.enums.RedirectType;
import java.util.Set;

import io.shelang.aghab.enums.WebhookStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
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

  @Enumerated(EnumType.ORDINAL)
  @Column(columnDefinition = "int2")
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
  @Column(columnDefinition = "redirect_type")
  @Type(PostgreSQLEnumType.class)
  private RedirectType type;

  @Column(name = "script_id")
  private Long scriptId;

  @Column(name = "webhook_id")
  private Long webhookId;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "webhook_status", columnDefinition = "int2")
  private WebhookStatus webhookStatus;
}
