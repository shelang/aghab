package io.shelang.aghab.service.link;

import io.shelang.aghab.service.dto.link.LinkAlternativeTypesDTO;
import io.shelang.aghab.service.dto.link.LinkCreateDTO;
import io.shelang.aghab.service.dto.link.LinkDTO;
import io.shelang.aghab.service.dto.link.LinksUserDTO;

public interface LinksService {

  LinksUserDTO get(String q, Integer page, Integer size);

  LinkDTO getById(Long id);

  LinkDTO getByHash(String hash);

  LinkDTO create(LinkCreateDTO links);

  void delete(Long id);

  LinkDTO update(Long id, LinkCreateDTO links);

  LinkAlternativeTypesDTO getLinkAlternativeTypes();
}
