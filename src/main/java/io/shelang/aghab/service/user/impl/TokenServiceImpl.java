package io.shelang.aghab.service.user.impl;

import io.shelang.aghab.domain.User;
import io.shelang.aghab.enums.JwtTokenType;
import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.LoginDTO;
import io.shelang.aghab.service.user.TokenService;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;
import java.time.Duration;
import java.time.Instant;

@ApplicationScoped
public class TokenServiceImpl implements TokenService {

  public static final String ISSUER = "aghab/issuer";
  private static final String CLAIM_TOKEN_TYPE = "t";

  @SuppressWarnings("CdiInjectionPointsInspection")
  @Inject
  JsonWebToken jwt;

  @Override
  public LoginDTO createTokens(User user) {
    String token =
        Jwt.issuer(ISSUER)
            .groups(user.getUsername().equalsIgnoreCase(Roles.BOSS) ? Roles.BOSS : Roles.USER)
            .claim(CLAIM_TOKEN_TYPE, JwtTokenType.ACCESS.ordinal())
            .upn(user.getUsername())
            .subject(user.getId().toString())
            .expiresIn(Duration.ofDays(7))
            .sign();

    String refresh =
        Jwt.issuer(ISSUER)
            .claim(CLAIM_TOKEN_TYPE, JwtTokenType.REFRESH.ordinal())
            .claim(REFRESH_CLAIM_USER_ID, user.getId())
            .groups(Roles.REFRESH_TOKEN)
            .expiresIn(Duration.ofDays(8))
            .sign();

    return new LoginDTO().setToken(token).setRefresh(refresh);
  }

  @Override
  public Long getAccessTokenUserId() {
    return Long.parseLong(jwt.getSubject());
  }

  @Override
  public String getRefreshTokenUserId() {
    return jwt.claim(TokenService.REFRESH_CLAIM_USER_ID)
        .orElseThrow(ForbiddenException::new)
        .toString();
  }

  @Override
  public String createAPIToken(User user) {
    return Jwt.issuer(ISSUER)
        .groups(Roles.API)
        .claim(CLAIM_TOKEN_TYPE, JwtTokenType.ACCESS.ordinal())
        .upn(user.getUsername())
        .subject(user.getId().toString())
        .issuedAt(Instant.now().plusSeconds(2))
        .expiresIn(Duration.ofDays(Long.MAX_VALUE))
        .sign();
  }
}
