package io.shelang.aghab.service.dto.workspace;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserWorkspaceRequest {
  Long userId;
  List<Long> workspaceIds;
}
