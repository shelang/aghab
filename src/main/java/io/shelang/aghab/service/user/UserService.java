package io.shelang.aghab.service.user;

import io.shelang.aghab.service.dto.UserCredentialDTO;
import io.shelang.aghab.service.dto.UserDTO;
import io.shelang.aghab.service.dto.UserMeDTO;
import io.shelang.aghab.service.dto.UsersDTO;

public interface UserService {

  UserDTO getById(Long id);

  UsersDTO get(String username, Integer page, Integer size);

  UserDTO create(UserCredentialDTO createDTO);

  UserDTO update(UserCredentialDTO createDTO);

  UserMeDTO getMe();

  UserMeDTO generateAPIToken();

  void delete(Long id);
}
