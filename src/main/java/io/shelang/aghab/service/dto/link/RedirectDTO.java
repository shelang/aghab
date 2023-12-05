package io.shelang.aghab.service.dto.link;

import io.shelang.aghab.enums.RedirectType;
import io.shelang.aghab.enums.WebhookStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class RedirectDTO {

  private long id;
  private RedirectType type;
  private String url;
  private String title;
  private String content;
  private String altKey;
  private String altUrl;
  private short statusCode;
  private Long scriptId;
  private Integer timeout;
  private Long webhookId;
  private WebhookStatus webhookStatus;
  private boolean forwardParameter;
}
