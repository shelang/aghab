package io.shelang.aghab.service.link;

import io.shelang.aghab.domain.*;
import io.shelang.aghab.enums.RedirectType;
import io.shelang.aghab.exception.MaxCreateLinkRetryException;
import io.shelang.aghab.repository.*;
import io.shelang.aghab.service.dto.*;
import io.shelang.aghab.service.mapper.LinkUserMapper;
import io.shelang.aghab.service.mapper.LinksMapper;
import io.shelang.aghab.service.script.ScriptService;
import io.shelang.aghab.service.shorty.Shorty;
import io.shelang.aghab.service.user.TokenService;
import io.shelang.aghab.service.webhook.WebhookService;
import io.shelang.aghab.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

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

  @ConfigProperty(name = "app.create.hash.length.default", defaultValue = "6")
  int defaultHashLength;

  @ConfigProperty(name = "app.create.redirect.base-url", defaultValue = "")
  String redirectBaseUrl;

  @Inject Shorty shortyService;
  @Inject TokenService tokenService;
  @Inject LinksRepository linksRepository;
  @Inject LinkExpirationRepository linkExpirationRepository;
  @Inject LinksMapper linksMapper;
  @Inject LinkUserMapper linkUserMapper;
  @Inject UserRepository userRepository;
  @Inject LinkUserRepository linkUserRepository;
  @Inject LinkAlternativeRepository linkAlternativeRepository;
  @Inject ScriptService scriptService;
  @Inject WebhookService webhookService;

  private Link findById(Long id) {
    return linksRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
  }

  @Override
  public LinksUserDTO get(String q, Integer page, Integer size) {
    page = NumberUtil.normalizeValue(page, 1) - 1;
    size = NumberUtil.normalizeValue(size, 10);

    if (size > 50) size = 50;

    List<LinkUser> result =
        linkUserRepository.page(tokenService.getAccessTokenUserId(), q, page, size);
    return new LinksUserDTO().setLinks(linkUserMapper.toDTO(result));
  }

  @Override
  public LinkDTO getById(Long id) {
    var link = findById(id);
    validateLinkUser(link.getHash());
    return linksMapper.toDTO(link);
  }

  @Override
  public LinkDTO getByHash(String hash) {
    validateLinkUser(hash);
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
        .status(dto.getStatus())
        .url(dto.getUrl())
        .linkMeta(linkMeta)
        .redirectCode(redirectCode)
        .forwardParameter(dto.isForwardParameter())
        .scriptId(dto.getScriptId())
        .webhookId(dto.getWebhookId())
        .type(RedirectType.from(dto.getType()))
        .build();
  }

  private Set<LinkAlternative> buildAlternatives(LinkCreateDTO dto, Link link) {
    Set<LinkAlternative> alternatives = new HashSet<>();

    for (LinkAlternativeDTO alternative : dto.getOsAlternatives()) {
      alternatives.add(
          LinkAlternative.builder()
              .id(new LinkAlternative.LinkAlternativeId(link.getId(), alternative.getKey()))
              .link(link)
              .key(alternative.getKey())
              .url(alternative.getUrl())
              .build());
    }

    for (LinkAlternativeDTO alternative : dto.getDeviceAlternatives()) {
      alternatives.add(
          LinkAlternative.builder()
              .id(new LinkAlternative.LinkAlternativeId(link.getId(), alternative.getKey()))
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
    else
      hash =
          shortyService.generate(
              Objects.nonNull(dto.getHashLength()) ? dto.getHashLength() : defaultHashLength);

    return hash;
  }

  private Link initCreation(LinkCreateDTO dto) {
    String hash = generateHash(dto);

    if (!dto.getUrl().contains("://")) dto.setUrl("http://" + dto.getUrl());

    var linkMeta = buildLinkMeta(dto);

    if (Objects.nonNull(dto.getScriptId())) {
      ScriptDTO byId = scriptService.getById(dto.getScriptId());
      dto.setType(RedirectType.SCRIPT.name()).setRedirectCode((short) 200);
      log.info("[CREATE LINK] script id {} exist.", byId.getId());
    } else {
      if (RedirectType.SCRIPT.equals(RedirectType.from(dto.getType())))
        dto.setType(RedirectType.REDIRECT.name());
    }

    if (Objects.nonNull(dto.getWebhookId())) {
      WebhookDTO byId = webhookService.getById(dto.getWebhookId());
      log.info("[CREATE LINK] webhook id {} exist.", byId.getId());
    }

    var link = buildLink(dto, hash, linkMeta);
    var alternatives = buildAlternatives(dto, link);

    link.setAlternatives(alternatives);
    linkMeta.setLink(link);

    return link;
  }

  private void reSetHash(Link link, Integer hashLength) {
    log.info("[link-service] re-set link hash {}", link);
    link.setHash(
        shortyService.generate(Objects.nonNull(hashLength) ? hashLength : defaultHashLength));
  }

  @Override
  @Transactional
  public LinkDTO create(LinkCreateDTO dto) {
    var user =
        userRepository
            .findByIdOptional(tokenService.getAccessTokenUserId())
            .orElseThrow(NotFoundException::new);
    byte retry = 0;
    if (dto.getHash() != null) retry = MAX_RETRY_COUNT - 1;
    var link = initCreation(dto);
    persistAndRetry(link, retry);
    persistLinkUser(link, user);
    if (dto.getExpireAt() != null) {
      linkExpirationRepository.persistAndFlush(
          LinkExpiration.builder()
              .linkId(link.getId())
              .expireAt(dto.getExpireAt())
              .link(link)
              .build());
    }
    var linkDTO = linksMapper.toDTO(link);
    var hostHeader = Objects.nonNull(dto.getHost()) ? dto.getHost() : "";
    var originHeader = Objects.nonNull(dto.getOrigin()) ? dto.getOrigin() : "";
    var host = (hostHeader.isBlank() ? originHeader : hostHeader);
    var rHost = host.isBlank() ? redirectBaseUrl : host;
    linkDTO.setRedirectTo(rHost + "/r/" + linkDTO.getHash());
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
      reSetHash(link, link.getHash().length());
      persistAndRetry(link, (byte) (retryCount + 1));
      return;
    }
    try {
      linksRepository.persistAndFlush(link);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      reSetHash(link, link.getHash().length());
      persistAndRetry(link, (byte) (retryCount + 1));
    }
  }

  @Override
  @Transactional
  public void delete(Long id) {
    var link = linksRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    var linkUserId = new LinkUser.LinkUserId(tokenService.getAccessTokenUserId(), link.getHash());
    var linkUser =
        linkUserRepository.findByIdOptional(linkUserId).orElseThrow(ForbiddenException::new);
    linksRepository.deleteById(id);
    linkUserRepository.deleteById(linkUser.getId());
  }

  private void updateAlternatives(
      Link link, List<LinkAlternativeDTO> os, List<LinkAlternativeDTO> devices) {
    var alternatives = new HashSet<LinkAlternativeDTO>();

    alternatives.addAll(os);
    alternatives.addAll(devices);

    var deleted = new HashSet<LinkAlternative>();
    for (var entity : link.getAlternatives()) {
      var entityType = Objects.nonNull(entity.getKey()) ? entity.getKey() : "";
      alternatives.stream()
          .filter(a -> entityType.equalsIgnoreCase(a.getKey()))
          .findFirst()
          .ifPresentOrElse(a -> entity.setUrl(a.getUrl()), () -> deleted.add(entity));
    }

    deleted.forEach(
        la ->
            linkAlternativeRepository.delete(
                "link_id = ?1 AND key = ?2", la.getId().getLinkId(), la.getId().getKey()));

    alternatives.removeIf(
        dto ->
            link.getAlternatives().stream()
                .anyMatch(a -> a.getKey().equalsIgnoreCase(dto.getKey())));

    alternatives.forEach(
        dto ->
            link.getAlternatives()
                .add(
                    LinkAlternative.builder()
                        .id(new LinkAlternative.LinkAlternativeId(link.getId(), dto.getKey()))
                        .link(link)
                        .key(dto.getKey())
                        .url(dto.getUrl())
                        .build()));
  }

  private void updateLinkMeta(Link link, LinkCreateDTO request) {
    link.getLinkMeta().setTitle(request.getTitle()).setDescription(request.getDescription());
  }

  private void updateExpireAt(Link link, Instant expireAt) {
    var linkExpiration = link.getLinkExpiration();
    if (Objects.nonNull(linkExpiration) && Objects.nonNull(expireAt)) {
      linkExpiration.setExpireAt(expireAt);
    } else if (Objects.nonNull(linkExpiration)) {
      linkExpirationRepository.deleteById(link.getId());
      link.setLinkExpiration(null);
    } else if (Objects.nonNull(expireAt)) {
      link.setLinkExpiration(
          new LinkExpiration().setLink(link).setLinkId(link.getId()).setExpireAt(expireAt));
    }
  }

  private void updateLinkRedirectionType(Link link, LinkCreateDTO request) {
    RedirectType newType = RedirectType.from(request.getType());
    if (RedirectType.SCRIPT.equals(newType) && Objects.nonNull(request.getScriptId())) {
      link.setScriptId(scriptService.getById(request.getScriptId()).getId())
          .setRedirectCode((short) 200);
    } else {
      link.setScriptId(null);
      if (Objects.nonNull(request.getRedirectCode())
          && (request.getRedirectCode() >= 301 || request.getRedirectCode() <= 308))
        link.setRedirectCode(request.getRedirectCode());
      else link.setRedirectCode((short) 301);
    }
    link.setType(newType);
  }

  private void updateWebHook(Link link, Long webhookId) {
    if (Objects.nonNull(webhookId)) {
      link.setWebhookId(webhookService.getById(webhookId).getId());
    } else {
      link.setWebhookId(null);
    }
  }

  private void updateLink(LinkCreateDTO request, Link link) {
    link.setStatus(request.getStatus());
    link.setUrl(request.getUrl());
    link.setForwardParameter(request.isForwardParameter());
  }

  @Override
  @Transactional
  public LinkDTO update(Long id, LinkCreateDTO request) {
    LinkUser linkUser = validateLinkUser(request.getHash());
    var link =
        linksRepository.findByIdOptional(linkUser.getLinkId()).orElseThrow(NotFoundException::new);
    updateAlternatives(link, request.getOsAlternatives(), request.getDeviceAlternatives());
    updateLinkMeta(link, request);
    updateExpireAt(link, request.getExpireAt());
    updateLinkRedirectionType(link, request);
    updateWebHook(link, request.getWebhookId());
    updateLink(request, link);
    linksRepository.persistAndFlush(link);
    linksRepository.getEntityManager().clear();
    return linksMapper.toDTO(linksRepository.findById(link.getId()));
  }

  private LinkUser validateLinkUser(String hash) {
    return linkUserRepository
        .findByIdOptional(
            new LinkUser.LinkUserId(tokenService.getAccessTokenUserId(), hash))
        .orElseThrow(ForbiddenException::new);
  }
}
