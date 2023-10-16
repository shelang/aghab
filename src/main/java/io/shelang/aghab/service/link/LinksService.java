package io.shelang.aghab.service.link;

import io.shelang.aghab.service.dto.link.*;

public interface LinksService {

  LinksDTO get(String q, Integer page, Integer size);

  LinksDTO getByWorkspace(String q, Long workspaceId, Integer page, Integer size);

  LinkDTO getById(Long id);

  LinkDTO getByHash(String hash);

  LinkDTO create(LinkCreateDTO links);

  void delete(Long id);

  LinkDTO update(Long id, LinkCreateDTO links);

  LinkAlternativeTypesDTO getLinkAlternativeTypes();
}
