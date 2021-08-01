package io.shelang.aghab.service.dto;

import io.shelang.aghab.enums.LinkStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.HeaderParam;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

  @Valid private List<LinkAlternativeDTO> alternatives = new ArrayList<>();

  @Min(150)
  private String title;

  @Min(255)
  private String description;

  @HeaderParam("Host")
  private String host;
}
