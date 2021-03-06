package io.shelang.aghab.service.link;

import io.shelang.aghab.service.dto.LinkCreateDTO;
import io.shelang.aghab.service.dto.LinkDTO;
import io.shelang.aghab.service.dto.LinksUserDTO;

public interface LinksService {

  LinksUserDTO get(Integer page, Integer size);

  LinkDTO getById(Long id);

  LinkDTO getByHash(String hash);

  LinkDTO create(LinkCreateDTO links);

  void delete(Long id);
}
