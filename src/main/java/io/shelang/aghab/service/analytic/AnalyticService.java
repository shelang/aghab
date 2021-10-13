package io.shelang.aghab.service.analytic;

import io.shelang.aghab.service.dto.AnalyticDTO;
import io.shelang.aghab.service.dto.AnalyticRequestDTO;

public interface AnalyticService {
  AnalyticDTO getAndCount(Long linkId, AnalyticRequestDTO request);
}
