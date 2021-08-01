package io.shelang.aghab.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class RedirectDTO {
  private long id;
  private String url;
  private String altKey;
  private String altUrl;
  private short statusCode;
  private boolean forwardParameter;
}
