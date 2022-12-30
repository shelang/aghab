package io.shelang.aghab.service.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class UserMeDTO {

  private Long id;
  private String username;
  private String token;
  private boolean needChangePassword;
}
