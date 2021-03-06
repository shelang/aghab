package io.shelang.aghab.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AnalyticDTO {
  private Long linkId;
  private long count;
  private Instant from;
  private Instant to;
}
