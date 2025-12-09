package io.shelang.aghab.service.dto.link;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LinkAlternativeDTO {

  @NotBlank
  @JsonAlias(value = "type")
  private String key;

  @NotBlank
  private String url;
}
