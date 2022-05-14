package io.shelang.aghab.service.mapper;

import io.shelang.aghab.domain.Script;
import io.shelang.aghab.service.dto.ScriptDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface ScriptMapper extends EntityMapper<ScriptDTO, Script> {

}
