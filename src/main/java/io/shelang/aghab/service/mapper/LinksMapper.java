package io.shelang.aghab.service.mapper;

import io.shelang.aghab.domain.Links;
import io.shelang.aghab.service.dto.LinkDTO;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "cdi",
    uses = {LinkMetaMapper.class})
public interface LinksMapper extends EntityMapper<LinkDTO, Links> {}
