package io.shelang.aghab.service.user;

import io.shelang.aghab.service.dto.auth.UserCredentialDTO;
import io.shelang.aghab.service.dto.auth.UserDTO;
import io.shelang.aghab.service.dto.auth.UserMeDTO;
import io.shelang.aghab.service.dto.auth.UsersDTO;
import java.util.List;

public interface UserService {

  UserDTO getById(Long id);

  UsersDTO getByIds(List<Long> ids);

  UsersDTO get(String username, Integer page, Integer size);

  UserDTO create(UserCredentialDTO createDTO);

  UserDTO update(UserCredentialDTO createDTO);

  UserMeDTO getMe();

  UserMeDTO generateAPIToken();

  void delete(Long id);
}
