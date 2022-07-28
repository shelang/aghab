package io.shelang.aghab.service.user.impl;

import io.shelang.aghab.repository.UserRepository;
import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.LoginDTO;
import io.shelang.aghab.service.ratelimiter.RateLimiter;
import io.shelang.aghab.service.user.AuthService;
import io.shelang.aghab.service.user.TokenService;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

  @Inject
  UserRepository userRepository;
  @Inject
  TokenService tokenService;
  @Inject
  RateLimiter rateLimiter;

  @Override
  public LoginDTO login(String username, String password) {
    rateLimiter.handleLoginAttempt(username);
    var user = userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
    if (!BCrypt.checkpw(password, user.getPassword())) {
      throw new BadRequestException("Wrong password");
    }
    return tokenService.createTokens(user);
  }

  @Override
  @RolesAllowed({Roles.REFRESH_TOKEN})
  public LoginDTO refresh(String authorization) {
    var user =
        userRepository
            .findByIdOptional(Long.valueOf(tokenService.getRefreshTokenUserId()))
            .orElseThrow(NotFoundException::new);
    return tokenService.createTokens(user);
  }
}
