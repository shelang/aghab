package io.shelang.aghab.service.dto.analytic;

import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AnalyticBucket {

  private long count;
  private long uniqCount;
  private Instant from;
  private Instant to;
}
