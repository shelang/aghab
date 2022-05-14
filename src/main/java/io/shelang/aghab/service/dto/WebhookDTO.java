package io.shelang.aghab.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class WebhookDTO {

  private Long id;
  private String name;
  private String url;

}
