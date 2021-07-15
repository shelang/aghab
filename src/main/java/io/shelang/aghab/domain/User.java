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
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(generator = "users_id_gen", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "users_id_gen", allocationSize = 1, sequenceName = "users_id_seq")
  private Long id;

  private String username;

  private String password;
}
