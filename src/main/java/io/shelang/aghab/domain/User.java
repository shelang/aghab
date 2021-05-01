package io.shelang.aghab.domain;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(generator = "users_id_gen", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "users_id_gen", allocationSize = 1, sequenceName = "users_id_seq")
  private Long id;

  private String username;

  private String password;

  public User() {}

  public Long getId() {
    return id;
  }

  public User setId(Long id) {
    this.id = id;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public User setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public User setPassword(String password) {
    this.password = password;
    return this;
  }
}
