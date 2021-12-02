package io.shelang.aghab.service.user.impl;

import io.quarkus.panache.common.Page;
import io.shelang.aghab.domain.User;
import io.shelang.aghab.repository.UserRepository;
import io.shelang.aghab.service.dto.UserCredentialDTO;
import io.shelang.aghab.service.dto.UserDTO;
import io.shelang.aghab.service.dto.UserMeDTO;
import io.shelang.aghab.service.dto.UsersDTO;
import io.shelang.aghab.service.mapper.UserMapper;
import io.shelang.aghab.service.mapper.UserMeMapper;
import io.shelang.aghab.service.user.TokenService;
import io.shelang.aghab.service.user.UserService;
import io.shelang.aghab.util.NumberUtil;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class UserServiceImpl implements UserService {

  @Inject UserRepository userRepository;
  @Inject UserMapper userMapper;
  @Inject UserMeMapper userMeMapper;
  @Inject TokenService tokenService;

  @Inject
  @Claim(standard = Claims.sub)
  Long userId;

  @Override
  public UserDTO getById(Long id) {
    return userMapper.toDTO(
        userRepository.findByIdOptional(id).orElseThrow(NotFoundException::new));
  }

  @Override
  public UsersDTO get(String username, Integer page, Integer size) {
    page = NumberUtil.normalizeValue(page, 1) - 1;
    size = NumberUtil.normalizeValue(size, 10);

    if (size > 50) size = 50;

    List<UserDTO> users = userMapper.toDTO(userRepository.search(username, Page.of(page, size)));
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
  public UserDTO update(UserCredentialDTO createDTO) {
    User user =
        userRepository.findByIdOptional(createDTO.getId()).orElseThrow(NotFoundException::new);
    user.setUsername(createDTO.getUsername())
        .setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
    return userMapper.toDTO(user);
  }

  @Override
  public UserMeDTO getMe() {
    return userMeMapper.toDTO(
        userRepository.findByIdOptional(userId).orElseThrow(NotFoundException::new));
  }

  @Override
  @Transactional
  public UserMeDTO generateAPIToken() {
    User user = userRepository.findByIdOptional(userId).orElseThrow(NotFoundException::new);

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
