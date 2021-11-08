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
@Table(name = "scripts")
public class Script {

  @Id
  @GeneratedValue(generator = "scripts_id_gen", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "scripts_id_gen", allocationSize = 1, sequenceName = "scripts_id_seq")
  private Long id;

  @Column(length = 50)
  private String name;

  @Builder.Default
  private Integer timeout = 10000;

  @Column(length = 140)
  private String title;

  private String content;

  @Builder.Default
  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "scripts")
  Set<User> users = new HashSet<>();
}
