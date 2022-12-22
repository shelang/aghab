package io.shelang.aghab.service.dto.link;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.shelang.aghab.enums.LinkStatus;
import io.shelang.aghab.enums.RedirectType;
import java.time.Instant;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(Include.NON_NULL)
public class LinkDTO {

  private Long id;
  private String hash;
  private String url;
  private Instant expireAt;
  private short redirectCode;
  private LinkStatus status;
  private boolean forwardParameter;
  private String redirectTo;
  private Set<LinkAlternativeDTO> alternatives;
  private Set<LinkAlternativeDTO> os;
  private Set<LinkAlternativeDTO> devices;
  private String title;
  private String description;
  private RedirectType type;
  private Long scriptId;
  private Long webhookId;
}
