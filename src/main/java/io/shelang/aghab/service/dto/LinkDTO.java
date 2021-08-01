package io.shelang.aghab.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(Include.NON_NULL)
public class LinkDTO {
  private Long id;
  private String hash;
  private String alias;
  private String url;
  private Integer status;
  private boolean forwardParameter;
  private String redirectTo;
  private LinkMetaDTO linkMetaDTO;
  private List<LinkAlternativeDTO> alternatives;
}
