package io.shelang.aghab.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "webhooks")
public class Webhook {

  @Id
  @GeneratedValue(generator = "webhooks_id_gen", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "webhooks_id_gen", allocationSize = 1, sequenceName = "webhooks_id_seq")
  private Long id;

  @Column(length = 50)
  private String name;

  private String url;

  @Builder.Default
  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "webhooks")
  Set<User> users = new HashSet<>();
}
