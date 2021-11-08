package io.shelang.aghab.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
