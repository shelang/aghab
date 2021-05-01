package io.shelang.aghab.service.link;

import io.shelang.aghab.service.dto.LinkCreateDTO;
import io.shelang.aghab.service.dto.LinksDTO;

public interface LinksService {

  LinksDTO getById(Long id);

  LinksDTO getByHash(String hash);

  LinksDTO create(LinkCreateDTO links);

  void delete(Long id);
}
