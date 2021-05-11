package io.shelang.aghab.service.dto;

import java.time.Instant;

public class AnalyticDTO {

  private Long linkId;
  private long count;
  private Instant from;
  private Instant to;

  public AnalyticDTO() {}

  public Long getLinkId() {
    return linkId;
  }

  public AnalyticDTO setLinkId(Long linkId) {
    this.linkId = linkId;
    return this;
  }

  public long getCount() {
    return count;
  }

  public AnalyticDTO setCount(long count) {
    this.count = count;
    return this;
  }

  public Instant getFrom() {
    return from;
  }

  public AnalyticDTO setFrom(Instant from) {
    this.from = from;
    return this;
  }

  public Instant getTo() {
    return to;
  }

  public AnalyticDTO setTo(Instant to) {
    this.to = to;
    return this;
  }
}
