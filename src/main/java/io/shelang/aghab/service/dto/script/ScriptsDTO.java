package io.shelang.aghab.service.dto.script;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ScriptsDTO {

  private List<ScriptDTO> scripts;

}
