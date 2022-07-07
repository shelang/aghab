package io.shelang.aghab.service.analytic;

import io.shelang.aghab.service.dto.AnalyticDTO;
import io.shelang.aghab.service.dto.AnalyticListDTO;
import io.shelang.aghab.service.dto.AnalyticRequestDTO;
import io.shelang.aghab.service.dto.AnalyticTimeRangeRequestDTO;

public interface AnalyticService {

  AnalyticDTO getAndCount(Long linkId, AnalyticRequestDTO request);

  AnalyticListDTO top5Devices(Long linkId, AnalyticTimeRangeRequestDTO request);

  AnalyticListDTO top5Oses(Long linkId, AnalyticTimeRangeRequestDTO request);

  AnalyticListDTO top5AgentNames(Long linkId, AnalyticTimeRangeRequestDTO request);

  AnalyticListDTO top5DeviceBrands(Long linkId, AnalyticTimeRangeRequestDTO request);

  AnalyticListDTO top5DeviceNames(Long linkId, AnalyticTimeRangeRequestDTO request);

  AnalyticListDTO top5OsVersion(Long linkId, AnalyticTimeRangeRequestDTO request);

  AnalyticListDTO getLast10UniqIP(AnalyticTimeRangeRequestDTO request);
}
