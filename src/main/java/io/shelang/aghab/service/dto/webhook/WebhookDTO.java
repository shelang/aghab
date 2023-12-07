package io.shelang.aghab.service.dto.webhook;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class WebhookDTO {

  private Long id;
  @NotBlank
  private String name;
  @NotBlank
  private String url;

}
