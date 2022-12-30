package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.shelang.aghab.domain.WorkspaceUser;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkspaceUserRepository implements
    PanacheRepositoryBase<WorkspaceUser, WorkspaceUser.WorkspaceUserId> {

  public void deleteByWorkspaceId(Long id) {
    delete("workspace_id = ?1", id);
  }

  public List<WorkspaceUser> findByUserId(Long id, Page page) {
    return find("user_id = ?1", id).page(page).list();
  }

  public List<WorkspaceUser> findByWorkspaceId(Long workspaceId, Page page) {
    return find("workspace_id = ?1", workspaceId).page(page).list();
  }
}
