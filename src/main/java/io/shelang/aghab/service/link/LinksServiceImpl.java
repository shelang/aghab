package io.shelang.aghab.service.link;

import io.shelang.aghab.domain.*;
import io.shelang.aghab.exception.MaxCreateLinkRetryException;
import io.shelang.aghab.repository.LinkExpirationRepository;
import io.shelang.aghab.repository.LinkUserRepository;
import io.shelang.aghab.repository.LinksRepository;
import io.shelang.aghab.repository.UsersRepository;
import io.shelang.aghab.service.dto.LinkCreateDTO;
import io.shelang.aghab.service.dto.LinksDTO;
import io.shelang.aghab.service.mapper.LinksMapper;
import io.shelang.aghab.service.shorty.Shorty;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.Objects;

@ApplicationScoped
public class LinksServiceImpl implements LinksService {

  private static final byte MAX_RETRY_COUNT = 10;

  @Inject
  @Claim(standard = Claims.upn)
  String username;

  @Inject JsonWebToken jwt;
  @Inject Shorty shortyService;
  @Inject LinksRepository linksRepository;
  @Inject LinkExpirationRepository linkExpirationRepository;
  @Inject LinksMapper linksMapper;
  @Inject UsersRepository usersRepository;
  @Inject LinkUserRepository linkUserRepository;

  private Links findById(Long id) {
    return linksRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
  }

  @Override
  public LinksDTO getById(Long id) {
    return linksMapper.toDTO(findById(id));
  }

  @Override
  public LinksDTO getByHash(String hash) {
    Links links = linksRepository.findByHash(hash).orElseThrow(NotFoundException::new);
    return linksMapper.toDTO(links);
  }

  private Links initCreation(LinkCreateDTO dto) {
    String hash;

    if (Objects.nonNull(dto.getHash()) && dto.getHash().length() > 0) hash = dto.getHash();
    else hash = shortyService.generate();

    if (!dto.getUrl().contains("://")) dto.setUrl("http://" + dto.getUrl());

    LinkMeta linkMeta =
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
  public LinksDTO create(LinkCreateDTO dto) {
    User user = usersRepository.findByUsername(username).orElseThrow(NotFoundException::new);
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
    linkUserRepository.persistAndFlush(new LinkUser(user.getId(), link.getHash()));
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
    Long userId = Long.valueOf(jwt.getClaim("id").toString());
    LinkUser.LinkUserId linkUserId = new LinkUser.LinkUserId(userId, link.getHash());
    linkUserRepository.findByIdOptional(linkUserId).orElseThrow(ForbiddenException::new);
    linksRepository.deleteById(id);
    linkUserRepository.deleteById(linkUserId);
  }
}
