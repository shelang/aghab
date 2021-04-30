package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.shelang.aghab.domain.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class UsersRepository implements PanacheRepository<User> {
  public Optional<User> findByUsername(String username) {
    return find("username", username).firstResultOptional();
  }
}
