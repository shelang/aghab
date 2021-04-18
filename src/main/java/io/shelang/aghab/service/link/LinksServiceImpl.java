package io.shelang.aghab.service.link;

import io.shelang.aghab.domain.LinkExpiration;
import io.shelang.aghab.domain.LinkMeta;
import io.shelang.aghab.domain.Links;
import io.shelang.aghab.exception.MaxCreateLinkRetryException;
import io.shelang.aghab.model.LinkCreateDTO;
import io.shelang.aghab.model.LinkDTO;
import io.shelang.aghab.repository.LinkExpirationRepository;
import io.shelang.aghab.repository.LinksRepository;
import io.shelang.aghab.service.shorty.Shorty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.Objects;

@ApplicationScoped
public class LinksServiceImpl implements LinksService {

  private final byte MAX_RETRY_COUNT = 10;

  @Inject Shorty shortyService;
  @Inject LinksRepository linksRepository;
  @Inject LinkExpirationRepository linkExpirationRepository;

  private Links findById(Long id) {
    return linksRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
  }

  @Override
  public LinkDTO getById(Long id) {
    return toDTO(findById(id));
  }

  @Override
  public LinkDTO getByHash(String hash) {
    Links links = linksRepository.findByHash(hash).orElseThrow(NotFoundException::new);
    return toDTO(links);
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
  public LinkDTO create(LinkCreateDTO dto) {
    byte retry = 0;
    if (dto.getHash() != null) retry = MAX_RETRY_COUNT - 1;
    Links link = initCreation(dto);
    persistAndRetry(link, retry);
    if (dto.getExpireAt() != null) {
      linkExpirationRepository.persistAndFlush(
          new LinkExpiration().setLinkId(link.getId()).setExpireAt(dto.getExpireAt()));
    }
    return toDTO(link);
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
  public boolean delete(Long id) {
    return linksRepository.deleteById(id);
  }

  @Override
  public LinkDTO put(Links links) {
    findById(links.getId());
    linksRepository.persistAndFlush(links);
    return toDTO(links);
  }

  private LinkDTO toDTO(Links link) {
    return new LinkDTO()
        .setId(link.getId())
        .setDescription(link.getLinkMeta().getDescription())
        .setTitle(link.getLinkMeta().getTitle())
        .setHash(link.getHash())
        .setUrl(link.getUrl())
        .setStatus(link.getStatus());
  }
}
