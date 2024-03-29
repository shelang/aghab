package io.shelang.aghab.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.shelang.aghab.domain.LinkWorkspace;
import java.util.List;
import java.util.Objects;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LinkWorkspaceRepository implements
    PanacheRepositoryBase<LinkWorkspace, LinkWorkspace.LinkWorkspaceId> {

  public List<LinkWorkspace> page(Long workspaceId, String hash, Page page) {
    PanacheQuery<LinkWorkspace> query;
    if (hasBothId(workspaceId, hash)) {
      query =
          find(
              "workspaceId = ?1 and linkHash = ?2 ",
              Sort.by("createAt", Sort.Direction.Descending),
              workspaceId,
              hash);
    } else if (hasWorkspaceId(workspaceId)){
      query = find("workspaceId = ?1", Sort.by("createAt", Sort.Direction.Descending),
          workspaceId);
    } else if (hasLinkHash(hash)) {
      query = find("linkHash = ?1", Sort.by("createAt", Sort.Direction.Descending),
          hash);
    } else {
      query = findAll(Sort.by("createAt", Sort.Direction.Descending));
    }
    return query.page(page).list();
  }

  private boolean hasLinkHash(String hash) {
    return Objects.nonNull(hash) && !hash.isEmpty();
  }

  private boolean hasWorkspaceId(Long workspaceId) {
    return Objects.nonNull(workspaceId);
  }

  private boolean hasBothId(Long workspaceId, String hash) {
    return hasWorkspaceId(workspaceId) && hasLinkHash(hash);
  }
}
