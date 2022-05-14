package io.shelang.aghab.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "link_alternatives")
public class LinkAlternative {

  @EmbeddedId
  private LinkAlternativeId id;

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
