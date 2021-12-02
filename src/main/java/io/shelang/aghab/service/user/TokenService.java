package io.shelang.aghab.service.user;

import io.shelang.aghab.domain.User;
import io.shelang.aghab.service.dto.LoginDTO;

public interface TokenService {
  String CLAIM_ID = "i";

  LoginDTO createTokens(User user);

  String getId();

  String createAPIToken(User user);
}
