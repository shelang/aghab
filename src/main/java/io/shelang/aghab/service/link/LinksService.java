package io.shelang.aghab.service.link;

import io.shelang.aghab.domain.Links;
import io.shelang.aghab.model.LinkCreateDTO;

public interface LinksService {

  Links getById(Long id);

  Links getByHash(String hash);

  Links create(LinkCreateDTO links);

  boolean delete(Long id);

  Links put(Links links);
}
