package io.shelang.aghab.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LoginDTO {
  private String token;
  private String refresh;
}
