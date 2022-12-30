package io.shelang.aghab.service.dto.workspace;

import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WorkspacesDTO {

  private List<WorkspaceDTO> workspaces = Collections.emptyList();

}
