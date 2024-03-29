package io.shelang.aghab.service.dto.auth;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class UsersDTO {

  private List<UserDTO> users;

}
