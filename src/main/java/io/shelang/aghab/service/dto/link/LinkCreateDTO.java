package io.shelang.aghab.service.dto.link;

import com.fasterxml.jackson.annotation.JsonAlias;

import io.shelang.aghab.enums.LinkStatus;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import io.shelang.aghab.validation.ValidURI;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class LinkCreateDTO {

  @NotBlank
  @ValidURI
  @Length(min = 5)
  private String url;

  @NotBlank
  private String type;

  private Short redirectCode = 301; // 301 .. 308

  private Instant expireAt;

  private Integer hashLength;

  private String hash;

  @Length(max = 150)
  private String title;

  @Length(max = 255)
  private String description;

  @Valid
  @JsonAlias(value = "os")
  private List<LinkAlternativeDTO> osAlternatives = new ArrayList<>();

  @Valid
  @JsonAlias(value = "devices")
  private List<LinkAlternativeDTO> deviceAlternatives = new ArrayList<>();

  private boolean forwardParameter;

  private Long scriptId;

  private Long webhookId;

  private LinkStatus status = LinkStatus.ACTIVE;

  private Long workspaceId;

  private String host;

  private String origin;
}
