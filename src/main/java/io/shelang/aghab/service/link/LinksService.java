package io.shelang.aghab.service.link;

import io.shelang.aghab.domain.Links;
import io.shelang.aghab.model.LinkCreateDTO;
import io.shelang.aghab.model.LinkDTO;

public interface LinksService {

  LinkDTO getById(Long id);

  LinkDTO getByHash(String hash);

  LinkDTO create(LinkCreateDTO links);

  boolean delete(Long id);

  LinkDTO put(Links links);
}
