package io.shelang.aghab.service.link;

import io.shelang.aghab.domain.LinkExpiration;
import io.shelang.aghab.domain.LinkMeta;
import io.shelang.aghab.domain.Links;
import io.shelang.aghab.exception.MaxCreateLinkRetryException;
import io.shelang.aghab.model.LinkCreateDTO;
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

  @Override
  public Links getById(Long id) {
    return linksRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
  }

  @Override
  public Links getByHash(String hash) {
    return linksRepository.findByHash(hash).orElseThrow(NotFoundException::new);
  }

  private Links initCreation(LinkCreateDTO dto) {
    String hash;

    if (Objects.nonNull(dto.getHash()) && dto.getHash().length() > 0) hash = dto.getHash();
    else hash = shortyService.generate();

    if (!dto.getUrl().contains("://")) dto.setUrl("http://" + dto.getUrl());

    LinkMeta linkMeta =
        new LinkMeta()
            .setCreatedAt(Instant.now())
            .setDescription(dto.getDescription())
            .setTitle(dto.getTitle());

    return new Links()
        .setHash(hash)
        .setStatus(dto.getStatus().ordinal())
        .setUrl(dto.getUrl())
        .setLinkMeta(linkMeta);
  }

  @Override
  @Transactional
  public Links create(LinkCreateDTO dto) {
    byte retry = 0;
    if (dto.getHash() != null) retry = MAX_RETRY_COUNT - 1;
    Links link = initCreation(dto);
    persistAndRetry(link, retry);
    if (dto.getExpireAt() != null) {
      linkExpirationRepository.persistAndFlush(
          new LinkExpiration().setLinkId(link.getId()).setExpire_date(dto.getExpireAt()));
    }
    return link;
  }

  private void persistAndRetry(Links link, byte retryCount) {
    if (retryCount >= MAX_RETRY_COUNT) throw new MaxCreateLinkRetryException();
    try {
      linksRepository.persistAndFlush(link);
    } catch (Exception e) {
      persistAndRetry(link, (byte) (retryCount + 1));
    }
    return;
  }

  @Override
  public boolean delete(Long id) {
    return linksRepository.deleteById(id);
  }

  @Override
  public Links put(Links links) {
    Links byId = getById(links.getId());
    linksRepository.persistAndFlush(links);
    return links;
  }
}
