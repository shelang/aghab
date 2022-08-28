package io.shelang.aghab.service.user.impl;

import io.shelang.aghab.domain.User;
import io.shelang.aghab.repository.UserRepository;
import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.UserCredentialDTO;
import io.shelang.aghab.service.dto.UserDTO;
import io.shelang.aghab.service.dto.UserMeDTO;
import io.shelang.aghab.service.dto.UsersDTO;
import io.shelang.aghab.service.mapper.UserMapper;
import io.shelang.aghab.service.mapper.UserMeMapper;
import io.shelang.aghab.service.user.TokenService;
import io.shelang.aghab.service.user.UserService;
import io.shelang.aghab.util.PageUtil;
import io.shelang.aghab.util.StringUtil;
import java.time.Instant;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class UserServiceImpl implements UserService {

  @Inject
  UserRepository userRepository;
  @Inject
  UserMapper userMapper;
  @Inject
  UserMeMapper userMeMapper;
  @Inject
  TokenService tokenService;
  @SuppressWarnings("CdiInjectionPointsInspection")
  @Inject
  JsonWebToken jwt;

  @Override
  public UserDTO getById(Long id) {
    return userMapper.toDTO(
        userRepository.findByIdOptional(id).orElseThrow(NotFoundException::new));
  }

  @Override
  public UsersDTO get(String username, Integer page, Integer size) {
    List<UserDTO> users = userMapper.toDTO(
        userRepository.search(username, PageUtil.of(page, size)));
    return new UsersDTO().setUsers(users);
  }

  @Override
  @Transactional
  public UserDTO create(UserCredentialDTO createDTO) {
    User user =
        new User()
            .setUsername(createDTO.getUsername())
            .setPassword(BCrypt.hashpw(createDTO.getPassword(), BCrypt.gensalt()));
    userRepository.persistAndFlush(user);
    return userMapper.toDTO(user);
  }

  @Override
  @Transactional
  public UserDTO update(UserCredentialDTO userCredentialDTO) {
    if (!jwt.getGroups().contains(Roles.BOSS) &&
        !jwt.getSubject().equalsIgnoreCase(userCredentialDTO.getId().toString())) {
      throw new ForbiddenException();
    }

    User user =
        userRepository.findByIdOptional(userCredentialDTO.getId())
            .orElseThrow(NotFoundException::new);
    if (StringUtil.nonNullOrEmpty(userCredentialDTO.getUsername())) {
      user.setUsername(userCredentialDTO.getUsername());
    }
    if (StringUtil.nonNullOrEmpty(userCredentialDTO.getPassword())) {
      user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
    }

    return userMapper.toDTO(user);
  }

  @Override
  public UserMeDTO getMe() {
    return userMeMapper.toDTO(
        userRepository
            .findByIdOptional(tokenService.getAccessTokenUserId())
            .orElseThrow(NotFoundException::new));
  }

  @Override
  @Transactional
  public UserMeDTO generateAPIToken() {
    User user =
        userRepository
            .findByIdOptional(tokenService.getAccessTokenUserId())
            .orElseThrow(NotFoundException::new);

    user.setTokenIssueAt(Instant.now());
    user.setToken(tokenService.createAPIToken(user));
    userRepository.persistAndFlush(user);

    return userMeMapper.toDTO(user);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    // TODO: soft delete
  }
}
