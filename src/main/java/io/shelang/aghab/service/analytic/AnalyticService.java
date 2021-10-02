package io.shelang.aghab.service.analytic;

import io.shelang.aghab.service.dto.AnalyticDTO;

public interface AnalyticService {
    AnalyticDTO getAndCount(Long linkId, String from, String to);
}
