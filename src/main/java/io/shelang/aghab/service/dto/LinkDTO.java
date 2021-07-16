package io.shelang.aghab.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class LinkDTO {
  private Long id;
  private String hash;
  private String alias;
  private String url;
  private Integer status;
  private boolean forwardParameter;
  private LinkMetaDTO linkMetaDTO;
}
