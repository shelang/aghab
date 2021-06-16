package io.shelang.aghab.service.link;

import io.shelang.aghab.domain.*;
import io.shelang.aghab.exception.MaxCreateLinkRetryException;
import io.shelang.aghab.repository.LinkExpirationRepository;
import io.shelang.aghab.repository.LinkUserRepository;
import io.shelang.aghab.repository.LinksRepository;
import io.shelang.aghab.repository.UsersRepository;
import io.shelang.aghab.service.dto.LinkCreateDTO;
import io.shelang.aghab.service.dto.LinkDTO;
import io.shelang.aghab.service.dto.LinksUserDTO;
import io.shelang.aghab.service.mapper.LinkUserMapper;
import io.shelang.aghab.service.mapper.LinksMapper;
import io.shelang.aghab.service.shorty.Shorty;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class LinksServiceImpl implements LinksService {

  private static final byte MAX_RETRY_COUNT = 10;

  @Inject
  @Claim(standard = Claims.upn)
  String username;

  @Inject
  @Claim(standard = Claims.sub)
  Long userId;

  @Inject Shorty shortyService;
  @Inject LinksRepository linksRepository;
  @Inject LinkExpirationRepository linkExpirationRepository;
  @Inject LinksMapper linksMapper;
  @Inject LinkUserMapper linkUserMapper;
  @Inject UsersRepository usersRepository;
  @Inject LinkUserRepository linkUserRepository;

  private Links findById(Long id) {
    return linksRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
  }

  private int normalizeValue(Integer value, int defaultValue) {
    if (Objects.isNull(value)) {
      value = defaultValue;
    }
    return value;
  }

  @Override
  public LinksUserDTO get(Integer page, Integer size) {
    page = normalizeValue(page, 1);
    size = normalizeValue(size, 10);

    if (size > 50) size = 50;

    List<LinkUser> result = linkUserRepository.page(userId, page - 1, size);
    return new LinksUserDTO().setLinks(linkUserMapper.toDTO(result));
  }

  @Override
  public LinkDTO getById(Long id) {
    return linksMapper.toDTO(findById(id));
  }

  @Override
  public LinkDTO getByHash(String hash) {
    var links = linksRepository.findByHash(hash).orElseThrow(NotFoundException::new);
    return linksMapper.toDTO(links);
  }

  private Links initCreation(LinkCreateDTO dto) {
    String hash;

    if (Objects.nonNull(dto.getHash()) && dto.getHash().length() > 0) hash = dto.getHash();
    else hash = shortyService.generate();

    if (!dto.getUrl().contains("://")) dto.setUrl("http://" + dto.getUrl());

    var linkMeta =
        new LinkMeta()
            .setCreateAt(Instant.now())
            .setDescription(dto.getDescription())
            .setTitle(dto.getTitle());

    Links link =
        new Links()
            .setHash(hash)
            .setStatus(dto.getStatus().ordinal())
            .setUrl(dto.getUrl())
            .setLinkMeta(linkMeta);

    linkMeta.setLinks(link);

    return link;
  }

  @Override
  @Transactional
  public LinkDTO create(LinkCreateDTO dto) {
    User user = usersRepository.findByIdOptional(userId).orElseThrow(NotFoundException::new);
    byte retry = 0;
    if (dto.getHash() != null) retry = MAX_RETRY_COUNT - 1;
    Links link = initCreation(dto);
    persistAndRetry(link, retry);
    persistLinkUser(link, user);
    if (dto.getExpireAt() != null) {
      linkExpirationRepository.persistAndFlush(
          new LinkExpiration().setLinkId(link.getId()).setExpireAt(dto.getExpireAt()));
    }
    return linksMapper.toDTO(link);
  }

  private void persistLinkUser(Links link, User user) {
    LinkUser linkUser =
        new LinkUser(user.getId(), link.getHash())
            .setLinkId(link.getId())
            .setCreateAt(link.getLinkMeta().getCreateAt());
    linkUserRepository.persistAndFlush(linkUser);
  }

  @Transactional
  private void persistAndRetry(Links link, byte retryCount) {
    if (retryCount >= MAX_RETRY_COUNT) throw new MaxCreateLinkRetryException();
    try {
      linksRepository.persistAndFlush(link);
    } catch (Exception e) {
      e.printStackTrace();
      persistAndRetry(link, (byte) (retryCount + 1));
    }
  }

  @Override
  @Transactional
  public void delete(Long id) {
    Links link = linksRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    var linkUserId = new LinkUser.LinkUserId(userId, link.getHash());
    linkUserRepository.findByIdOptional(linkUserId).orElseThrow(ForbiddenException::new);
    linksRepository.deleteById(id);
    linkUserRepository.deleteById(linkUserId);
  }
}
