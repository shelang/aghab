package io.shelang.aghab.service.dto.workspace;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WorkspaceDTO {

  private Long id;
  private String name;
}
