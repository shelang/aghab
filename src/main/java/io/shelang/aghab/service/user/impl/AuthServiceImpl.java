package io.shelang.aghab.service.user.impl;

import io.shelang.aghab.domain.User;
import io.shelang.aghab.enums.JwtTokenType;
import io.shelang.aghab.repository.UserRepository;
import io.shelang.aghab.service.dto.LoginDTO;
import io.shelang.aghab.service.user.AuthService;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import java.time.Duration;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

  private static final String CLAIM_TOKEN_TYPE = "t";
  private static final String CLAIM_ID = "i";

  @SuppressWarnings("CdiInjectionPointsInspection")
  @Inject JsonWebToken jwt;
  @Inject
  UserRepository userRepository;

  private LoginDTO createTokens(User user) {
    String token =
        Jwt.issuer("aghab/issuer")
            .claim(CLAIM_TOKEN_TYPE, JwtTokenType.ACCESS.ordinal())
            .upn(user.getUsername())
            .subject(user.getId().toString())
            .expiresIn(Duration.ofDays(7))
            .sign();

    String refresh =
        Jwt.issuer("aghab/issuer")
            .claim(CLAIM_TOKEN_TYPE, JwtTokenType.REFRESH.ordinal())
            .claim(CLAIM_ID, user.getId())
            .expiresIn(Duration.ofDays(8))
            .sign();

    return new LoginDTO().setToken(token).setRefresh(refresh);
  }

  @Override
  public LoginDTO login(String username, String password) {
    var user = userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
    if (!BCrypt.checkpw(password, user.getPassword())) {
      throw new BadRequestException("Wrong password");
    }
    return createTokens(user);
  }

  private void validateRefreshToken() {
    var refreshTokenNotFound = new ForbiddenException("Use refresh token");
    boolean isRefresh = jwt.claim(CLAIM_TOKEN_TYPE)
            .orElseThrow(() -> refreshTokenNotFound)
            .equals(JwtTokenType.REFRESH.ordinal());
    if (!isRefresh) {
      throw refreshTokenNotFound;
    }
  }

  @Override
  public LoginDTO refresh(String authorization) {
    validateRefreshToken();
    var id = jwt.claim(CLAIM_ID).orElseThrow(ForbiddenException::new).toString();
    var user = userRepository.findByIdOptional(Long.valueOf(id)).orElseThrow(NotFoundException::new);
    return createTokens(user);
  }
}
