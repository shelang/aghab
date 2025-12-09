package io.shelang.aghab.service.user.impl;

import io.shelang.aghab.domain.User;
import io.shelang.aghab.repository.UserRepository;
import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.auth.LoginDTO;
import io.shelang.aghab.service.dto.workspace.WorkspacesDTO;
import io.shelang.aghab.service.ratelimiter.RateLimiter;
import io.shelang.aghab.service.user.AuthService;
import io.shelang.aghab.service.user.TokenService;
import io.shelang.aghab.service.workspace.WorkspaceService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

  @Inject
  UserRepository userRepository;
  @Inject
  TokenService tokenService;
  @Inject
  RateLimiter rateLimiter;
  @Inject
  WorkspaceService workspaceService;

  @Inject
  io.shelang.aghab.service.audit.AuditService auditService;
  @Inject
  io.vertx.core.http.HttpServerRequest request;

  @Override
  public LoginDTO login(String username, String password) {
    rateLimiter.handleLoginAttempt(username);
    var user = userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
    if (!BCrypt.checkpw(password, user.getPassword())) {
      throw new BadRequestException("Wrong password");
    }
    auditService.log(user.getId(), null, "LOGIN_SUCCESS", "User logged in", request.remoteAddress().host());
    return getLoginDTO(user);
  }

  @Override
  @RolesAllowed({ Roles.REFRESH_TOKEN })
  public LoginDTO refresh(String authorization) {
    var user = userRepository
        .findByIdOptional(Long.valueOf(tokenService.getRefreshTokenUserId()))
        .orElseThrow(NotFoundException::new);
    return getLoginDTO(user);
  }

  private LoginDTO getLoginDTO(User user) {
    LoginDTO loginDTO = tokenService.createTokens(user);
    WorkspacesDTO userWorkspaces = workspaceService.getUserWorkspaces(user.getId(), 1, 50);
    loginDTO.setWorkspaces(userWorkspaces.getWorkspaces());
    return loginDTO;
  }
}
