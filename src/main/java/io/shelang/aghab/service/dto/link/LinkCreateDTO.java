package io.shelang.aghab.service.dto.link;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.shelang.aghab.enums.LinkStatus;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.HeaderParam;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LinkCreateDTO {

  @NotBlank
  private String url;
  private String hash;
  private Integer hashLength;
  private Instant expireAt;
  private Short redirectCode = 301;
  private LinkStatus status = LinkStatus.ACTIVE;
  private boolean forwardParameter;
  private Long workspaceId;

  @Valid
  @JsonAlias(value = "os")
  private List<LinkAlternativeDTO> osAlternatives = new ArrayList<>();

  @Valid
  @JsonAlias(value = "devices")
  private List<LinkAlternativeDTO> deviceAlternatives = new ArrayList<>();

  @Length(max = 150)
  private String title;

  @Length(max = 255)
  private String description;

  private String type;

  private Long scriptId;

  private Long webhookId;

  @HeaderParam("Host")
  private String host;

  @HeaderParam("Origin")
  private String origin;
}
