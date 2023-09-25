package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.shelang.aghab.domain.WorkspaceUser;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkspaceUserRepository implements
    PanacheRepositoryBase<WorkspaceUser, WorkspaceUser.WorkspaceUserId> {

  public void deleteByWorkspaceId(Long id) {
    delete("workspaceId = ?1", id);
  }

  public List<WorkspaceUser> findByUserId(Long id, Page page) {
    return find("userId = ?1", id).page(page).list();
  }

  public List<WorkspaceUser> findByWorkspaceId(Long workspaceId, Page page) {
    return find("workspaceId = ?1", workspaceId).page(page).list();
  }
}
