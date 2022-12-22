package io.shelang.aghab.service.mapper;

import io.shelang.aghab.domain.LinkMeta;
import io.shelang.aghab.service.dto.link.LinkMetaDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface LinkMetaMapper extends EntityMapper<LinkMetaDTO, LinkMeta> {

}
