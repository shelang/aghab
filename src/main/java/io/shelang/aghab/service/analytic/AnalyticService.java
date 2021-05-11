package io.shelang.aghab.service.analytic;

import io.shelang.aghab.service.dto.AnalyticDTO;

import java.util.Date;

public interface AnalyticService {
    AnalyticDTO getAndCount(Long linkId, Date from, Date to);
}
