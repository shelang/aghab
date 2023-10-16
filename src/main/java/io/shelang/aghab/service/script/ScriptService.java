package io.shelang.aghab.service.script;

import io.shelang.aghab.service.dto.script.ScriptDTO;
import java.util.List;

public interface ScriptService {

  ScriptDTO getById(Long id);

  ScriptDTO getByIdAndValidation(Long id);

  List<ScriptDTO> get(String name, Integer page, Integer size);

  ScriptDTO create(ScriptDTO dto);

  ScriptDTO update(ScriptDTO dto);

}
