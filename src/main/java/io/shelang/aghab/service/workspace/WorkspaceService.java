package io.shelang.aghab.service.workspace;

import io.shelang.aghab.service.dto.auth.UsersDTO;
import io.shelang.aghab.service.dto.workspace.MembersRequest;
import io.shelang.aghab.service.dto.workspace.UserWorkspaceRequest;
import io.shelang.aghab.service.dto.workspace.WorkspaceDTO;
import io.shelang.aghab.service.dto.workspace.WorkspacesDTO;

public interface WorkspaceService {

  WorkspaceDTO getWorkspace(Long id);
  WorkspacesDTO search(String q, Integer page, Integer size);
  WorkspaceDTO create(WorkspaceDTO request);
  WorkspaceDTO update(WorkspaceDTO request);
  void delete(Long id);
  void addMember(Long id, MembersRequest request);
  void deleteMember(Long id, MembersRequest request);
  UsersDTO listMember(Long id, Integer page, Integer size);
  WorkspacesDTO getUserWorkspaces(Integer page, Integer size);
  WorkspacesDTO getUserWorkspaces(Long userId, Integer page, Integer size);
  void addUserWorkspaces(UserWorkspaceRequest request);
  void deleteUserWorkspaces(UserWorkspaceRequest request);
}
