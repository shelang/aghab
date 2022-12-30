package io.shelang.aghab.service.mapper;

import io.shelang.aghab.domain.Workspace;
import io.shelang.aghab.service.dto.workspace.WorkspaceDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface WorkspaceMapper extends EntityMapper<WorkspaceDTO, Workspace> {

}
