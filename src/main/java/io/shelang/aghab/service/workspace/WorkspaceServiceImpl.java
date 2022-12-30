package io.shelang.aghab.service.workspace;

import io.shelang.aghab.domain.Workspace;
import io.shelang.aghab.domain.WorkspaceUser;
import io.shelang.aghab.domain.WorkspaceUser.WorkspaceUserId;
import io.shelang.aghab.repository.WorkspaceRepository;
import io.shelang.aghab.repository.WorkspaceUserRepository;
import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.auth.UsersDTO;
import io.shelang.aghab.service.dto.workspace.MembersRequest;
import io.shelang.aghab.service.dto.workspace.WorkspaceDTO;
import io.shelang.aghab.service.dto.workspace.WorkspacesDTO;
import io.shelang.aghab.service.mapper.WorkspaceMapper;
import io.shelang.aghab.service.user.TokenService;
import io.shelang.aghab.service.user.UserService;
import io.shelang.aghab.util.PageUtil;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

  final WorkspaceRepository workspaceRepository;
  final WorkspaceMapper workspaceMapper;
  final WorkspaceUserRepository workspaceUserRepository;
  final UserService userService;
  final TokenService tokenService;
  final JsonWebToken jwt;

  @Override
  public WorkspaceDTO getWorkspace(Long id) {
    return workspaceMapper.toDTO(getValidatedWorkspace(id));
  }

  @Override
  public WorkspacesDTO search(String q, Integer page, Integer size) {
    return new WorkspacesDTO().setWorkspaces(
        workspaceMapper.toDTO(workspaceRepository.search(q, tokenService.getAccessTokenUserId(),
            PageUtil.of(page, size))));
  }

  @Override
  @Transactional
  public WorkspaceDTO create(WorkspaceDTO request) {
    Workspace workspace = workspaceMapper.toEntity(request);
    workspace.setId(null);
    workspace.setCreatorUserId(tokenService.getAccessTokenUserId());
    workspaceRepository.persistAndFlush(workspace);
    saveWorkspaceUser(workspace);
    return workspaceMapper.toDTO(workspace);
  }

  @Override
  @Transactional
  public WorkspaceDTO update(WorkspaceDTO request) {
    hasOwnershipOnIt(request.getId());
    Workspace workspace = getValidatedWorkspace(request.getId());
    if (Objects.nonNull(request.getName())) {
      workspace.setName(request.getName());
    }
    workspaceRepository.persistAndFlush(workspace);
    return workspaceMapper.toDTO(workspace);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    hasOwnershipOnIt(id);
    workspaceUserRepository.deleteByWorkspaceId(id);
    workspaceRepository.deleteById(id);
  }

  @Override
  @Transactional
  public void addMember(Long id, MembersRequest request) {
    hasOwnershipOnIt(id);
    for (Long memberId : request.getMemberIds()) {
      WorkspaceUser workspaceUser = WorkspaceUser
          .builder()
          .id(new WorkspaceUser.WorkspaceUserId(memberId, id))
          .createAt(Instant.now())
          .build();
      workspaceUserRepository.persist(workspaceUser);
    }
    workspaceUserRepository.flush();
  }

  @Override
  @Transactional
  public void deleteMember(Long id, MembersRequest request) {
    hasOwnershipOnIt(id);
    for (Long memberId : request.getMemberIds()) {
      WorkspaceUserId workspaceUserId = new WorkspaceUserId(memberId, id);
      workspaceUserRepository.deleteById(workspaceUserId);
    }
  }

  @Override
  public UsersDTO listMember(Long workspaceId, Integer page, Integer size) {
    List<WorkspaceUser> workspaceUsers = workspaceUserRepository.findByWorkspaceId(workspaceId,
        PageUtil.of(page, size));
    return userService.getByIds(
        workspaceUsers.stream().map(WorkspaceUser::getUserId).collect(Collectors.toList()));
  }

  @Override
  public WorkspacesDTO getUserWorkspaces(Integer page, Integer size) {
    List<WorkspaceUser> workspaceUsers = workspaceUserRepository.findByUserId(
        tokenService.getAccessTokenUserId(), PageUtil.of(page, size));
    List<Long> ids = workspaceUsers.stream()
        .map(WorkspaceUser::getWorkspaceId)
        .collect(Collectors.toList());
    List<WorkspaceDTO> workspaces = ids.stream()
        .map(workspaceRepository::findByIdOptional)
        .flatMap(Optional::stream)
        .map(workspaceMapper::toDTO)
        .collect(Collectors.toList());
    return new WorkspacesDTO().setWorkspaces(workspaces);
  }

  private void hasOwnershipOnIt(Long id) {
    Workspace workspace = getValidatedWorkspace(id);
    if (!jwt.getGroups().contains(Roles.BOSS) ||
        !tokenService.getAccessTokenUserId().equals(workspace.getCreatorUserId())) {
      throw new ForbiddenException();
    }
  }

  private void saveWorkspaceUser(Workspace workspace) {
    WorkspaceUser workspaceUser = makeWebhookUser(workspace.getId());
    Optional<WorkspaceUser> exist = workspaceUserRepository.findByIdOptional(
        workspaceUser.getId());
    if (exist.isEmpty()) {
      workspaceUserRepository.persistAndFlush(workspaceUser);
    }
  }

  private Workspace getValidatedWorkspace(Long id) {
    WorkspaceUser workspaceUser = getWorkspaceUser(id).orElseThrow(ForbiddenException::new);
    Workspace webhook = workspaceRepository.findByIdOptional(id)
        .orElseThrow(NotFoundException::new);
    if (!tokenService.getAccessTokenUserId().equals(workspaceUser.getUserId())) {
      throw new ForbiddenException();
    }
    return webhook;
  }

  private Optional<WorkspaceUser> getWorkspaceUser(Long id) {
    return workspaceUserRepository.findByIdOptional(makeWebhookUser(id).getId());
  }

  private WorkspaceUser makeWebhookUser(Long id) {
    return new WorkspaceUser()
        .setWorkspaceId(id)
        .setUserId(tokenService.getAccessTokenUserId())
        .setId(getWorkspaceUserId(id));
  }

  private WorkspaceUser.WorkspaceUserId getWorkspaceUserId(Long id) {
    return new WorkspaceUser.WorkspaceUserId(tokenService.getAccessTokenUserId(), id);
  }
}
