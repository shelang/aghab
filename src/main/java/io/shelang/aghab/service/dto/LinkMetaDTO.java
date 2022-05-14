package io.shelang.aghab.service.dto;

import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LinkMetaDTO {

  private Long id;
  private String title;
  private String description;
  private Instant createAt;
  private Instant updateAt;
}
