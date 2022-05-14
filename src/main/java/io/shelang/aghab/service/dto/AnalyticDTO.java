package io.shelang.aghab.service.dto;

import java.time.Instant;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AnalyticDTO {

  private Long linkId;
  private long count;
  private Instant from;
  private Instant to;
  private List<AnalyticBucket> buckets;
}
