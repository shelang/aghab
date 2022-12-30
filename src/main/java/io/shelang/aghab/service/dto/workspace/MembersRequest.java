package io.shelang.aghab.service.dto.workspace;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class MembersRequest {

  List<Long> memberIds;
}
