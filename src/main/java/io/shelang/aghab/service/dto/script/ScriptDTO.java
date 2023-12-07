package io.shelang.aghab.service.dto.script;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ScriptDTO {

  private Long id;
  @NotBlank
  private String name;
  private Integer timeout;
  private String title;
  @NotBlank
  private String content;

}
