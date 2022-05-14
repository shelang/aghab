package io.shelang.aghab.service.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LinkAlternativeDTO {

  @NotEmpty
  @JsonAlias(value = "type")
  private String key;
  private String url;
}
