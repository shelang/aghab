package io.shelang.aghab.service.user.login;

import io.shelang.aghab.domain.User;
import io.shelang.aghab.enums.JwtTokenType;
import io.shelang.aghab.repository.UsersRepository;
import io.shelang.aghab.service.dto.LoginDTO;
import io.shelang.aghab.service.user.UserLoginService;
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
public class UserLoginServiceImpl implements UserLoginService {

  private static final String CLAIM_TOKEN_TYPE = "t";
  private static final String CLAIM_ID = "i";

  @Inject JsonWebToken jwt;
  @Inject UsersRepository usersRepository;

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
    var user = usersRepository.findByUsername(username).orElseThrow(NotFoundException::new);
    if (!BCrypt.checkpw(password, user.getPassword())) {
      throw new BadRequestException("Wrong password");
    }
    return createTokens(user);
  }

  private void validateRefreshToken() {
    jwt.claim(CLAIM_TOKEN_TYPE)
        .orElseThrow(() -> new ForbiddenException("Use refresh token"))
        .equals(JwtTokenType.REFRESH.ordinal());
  }

  @Override
  public LoginDTO refresh(String authorization) {
    validateRefreshToken();
    String id = jwt.claim(CLAIM_ID).orElseThrow(ForbiddenException::new).toString();
    var user = usersRepository.findByIdOptional(Long.valueOf(id)).orElseThrow(NotFoundException::new);
    return createTokens(user);
  }
}
