package io.shelang.aghab.service.dto;

import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LinkUserDTO {

  private Long userId;
  private Long linkId;
  private String linkHash;
  private Instant createAt;
}
