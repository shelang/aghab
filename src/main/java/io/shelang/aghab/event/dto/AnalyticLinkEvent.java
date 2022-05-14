package io.shelang.aghab.event.dto;

import io.vertx.core.MultiMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AnalyticLinkEvent {

  private Long id;
  private String hash;
  private MultiMap headers;

}
