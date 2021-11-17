package io.shelang.aghab.service.user;

import io.shelang.aghab.service.dto.LoginDTO;

public interface AuthService {

    LoginDTO login(String username, String password);

    LoginDTO refresh(String authorization);

}
