package io.shelang.aghab.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "link_alternatives")
public class LinkAlternative {

  @EmbeddedId private LinkAlternativeId id;

  @MapsId("linkId")
  @JoinColumn(insertable = false, updatable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Link link;

  @Column(length = 50, insertable = false, updatable = false)
  private String key;

  private String url;

  @Embeddable
  @Getter
  @Setter
  @ToString
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode
  public static class LinkAlternativeId implements Serializable {
    @Column(name = "link_id")
    private Long linkId;

    @Column(name = "key")
    private String key;
  }
}
