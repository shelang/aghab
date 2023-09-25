package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.shelang.aghab.domain.ScriptUser;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ScriptUserRepository
    implements PanacheRepositoryBase<ScriptUser, ScriptUser.ScriptUserId> {

}
