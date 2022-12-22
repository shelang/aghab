package io.shelang.aghab.service.user;

import io.shelang.aghab.service.dto.auth.LoginDTO;

public interface AuthService {

  LoginDTO login(String username, String password);

  LoginDTO refresh(String authorization);

}
