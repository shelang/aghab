package io.shelang.aghab.service.dto.analytic;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AnalyticTimeRangeRequestDTO {

  String from;
  String to;
}
