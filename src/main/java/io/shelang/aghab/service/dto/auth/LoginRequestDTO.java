package io.shelang.aghab.service.dto.auth;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LoginRequestDTO {

  @NotBlank
  private String username;

  @NotBlank
  private String password;
}
