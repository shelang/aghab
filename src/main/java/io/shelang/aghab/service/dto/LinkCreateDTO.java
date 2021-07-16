package io.shelang.aghab.service.dto;

import io.shelang.aghab.enums.LinkStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LinkCreateDTO {
  @NotBlank private String url;
  private String hash;
  private Instant expireAt;
  private Short redirectCode = 301;
  private LinkStatus status = LinkStatus.ACTIVE;
  private boolean forwardParameter;

  @Min(150)
  private String title;

  @Min(255)
  private String description;
}
