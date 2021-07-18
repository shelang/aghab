package io.shelang.aghab.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LoginRequestDTO {
  @NotBlank
  @Max(200)
  private String username;

  @NotBlank
  @Max(64)
  private String password;
}
