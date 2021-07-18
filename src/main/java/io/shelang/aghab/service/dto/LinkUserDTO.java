package io.shelang.aghab.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LinkUserDTO {
  private Long userId;
  private Long linkId;
  private String linkHash;
  private Instant createAt;
}
