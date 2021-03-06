package io.shelang.aghab.service.link;

import io.shelang.aghab.domain.*;
import io.shelang.aghab.exception.MaxCreateLinkRetryException;
import io.shelang.aghab.repository.LinkExpirationRepository;
import io.shelang.aghab.repository.LinkUserRepository;
import io.shelang.aghab.repository.LinksRepository;
import io.shelang.aghab.repository.UsersRepository;
import io.shelang.aghab.service.dto.LinkAlternativeDTO;
import io.shelang.aghab.service.dto.LinkCreateDTO;
import io.shelang.aghab.service.dto.LinkDTO;
import io.shelang.aghab.service.dto.LinksUserDTO;
import io.shelang.aghab.service.mapper.LinkUserMapper;
import io.shelang.aghab.service.mapper.LinksMapper;
import io.shelang.aghab.service.shorty.Shorty;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
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

  private Link findById(Long id) {
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

  private LinkMeta buildLinkMeta(LinkCreateDTO dto) {
    return LinkMeta.builder()
        .createAt(Instant.now())
        .description(dto.getDescription())
        .title(dto.getTitle())
        .build();
  }

  private Link buildLink(LinkCreateDTO dto, String hash, LinkMeta linkMeta) {
    Short redirectCode = Objects.nonNull(dto.getRedirectCode()) ? dto.getRedirectCode() : 301;
    return Link.builder()
        .hash(hash)
        .status(dto.getStatus().ordinal())
        .url(dto.getUrl())
        .linkMeta(linkMeta)
        .redirectCode(redirectCode)
        .forwardParameter(dto.isForwardParameter())
        .build();
  }

  private Set<LinkAlternative> buildAlternatives(LinkCreateDTO dto, Link link) {
    Set<LinkAlternative> alternatives = new HashSet<>();

    for (LinkAlternativeDTO alternative : dto.getAlternatives()) {
      alternatives.add(
          LinkAlternative.builder()
              .link(link)
              .key(alternative.getKey())
              .url(alternative.getUrl())
              .build());
    }

    return alternatives;
  }

  private String generateHash(LinkCreateDTO dto) {
    String hash;

    if (Objects.nonNull(dto.getHash()) && dto.getHash().length() > 0) hash = dto.getHash();
    else hash = shortyService.generate();

    return hash;
  }

  private Link initCreation(LinkCreateDTO dto) {
    String hash = generateHash(dto);

    if (!dto.getUrl().contains("://")) dto.setUrl("http://" + dto.getUrl());

    var linkMeta = buildLinkMeta(dto);

    var link = buildLink(dto, hash, linkMeta);
    var alternatives = buildAlternatives(dto, link);

    link.setAlternatives(alternatives);
    linkMeta.setLinks(link);

    return link;
  }

  private void reSetHash(Link link) {
    log.info("[link-service] re-set link hash {}", link);
    link.setHash(shortyService.generate());
  }

  @Override
  @Transactional
  public LinkDTO create(LinkCreateDTO dto) {
    var user = usersRepository.findByIdOptional(userId).orElseThrow(NotFoundException::new);
    byte retry = 0;
    if (dto.getHash() != null) retry = MAX_RETRY_COUNT - 1;
    var link = initCreation(dto);
    persistAndRetry(link, retry);
    persistLinkUser(link, user);
    if (dto.getExpireAt() != null) {
      linkExpirationRepository.persistAndFlush(
          LinkExpiration.builder().linkId(link.getId()).expireAt(dto.getExpireAt()).build());
    }
    var linkDTO = linksMapper.toDTO(link);
    var host = Objects.nonNull(dto.getHost()) ? dto.getHost() : "";
    linkDTO.setRedirectTo("https://" + host + "/r/" + linkDTO.getHash());
    return linkDTO;
  }

  @Transactional
  private void persistLinkUser(Link link, User user) {
    var linkUser = new LinkUser(user.getId(), link.getHash());
    linkUser.setLinkId(link.getId());
    linkUser.setCreateAt(link.getLinkMeta().getCreateAt());
    linkUserRepository.persistAndFlush(linkUser);
  }

  @Transactional
  private void persistAndRetry(Link link, byte retryCount) {
    if (retryCount >= MAX_RETRY_COUNT) throw new MaxCreateLinkRetryException();
    var existLink = linksRepository.findByHash(link.getHash()).orElse(null);
    if (Objects.nonNull(existLink)) {
      reSetHash(link);
      persistAndRetry(link, (byte) (retryCount + 1));
      return;
    }
    try {
      linksRepository.persistAndFlush(link);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      reSetHash(link);
      persistAndRetry(link, (byte) (retryCount + 1));
    }
  }

  @Override
  @Transactional
  public void delete(Long id) {
    var link = linksRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    var linkUserId = new LinkUser.LinkUserId(userId, link.getHash());
    var linkUser =
        linkUserRepository.findByIdOptional(linkUserId).orElseThrow(ForbiddenException::new);
    linksRepository.deleteById(id);
    linkUserRepository.deleteById(linkUser.getId());
  }
}
