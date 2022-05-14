package io.shelang.aghab.service.user;

import io.shelang.aghab.domain.User;
import io.shelang.aghab.service.dto.LoginDTO;

public interface TokenService {

  String REFRESH_CLAIM_USER_ID = "i";

  LoginDTO createTokens(User user);

  Long getAccessTokenUserId();

  String getRefreshTokenUserId();

  String createAPIToken(User user);
}
