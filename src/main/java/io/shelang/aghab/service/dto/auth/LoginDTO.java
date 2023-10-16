package io.shelang.aghab.service.dto.auth;

import io.shelang.aghab.service.dto.workspace.WorkspaceDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LoginDTO {

  private String token;
  private String refresh;
  private List<WorkspaceDTO> workspaces;

}
