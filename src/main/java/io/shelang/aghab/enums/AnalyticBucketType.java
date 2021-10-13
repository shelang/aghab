package io.shelang.aghab.enums;

import java.util.Optional;

public enum AnalyticBucketType {
  HOUR("hour"),
  HOURLY("hour"),
  DAILY("day"),
  WEEKLY("week"),
  MONTHLY("month"),
  QUARTERLY("quarter"),
  YEARLY("year");

  private final String type;

  AnalyticBucketType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public static Optional<AnalyticBucketType> from(String type) {
    try {
        return Optional.of(AnalyticBucketType.valueOf(type.toUpperCase()));
    } catch (NullPointerException | IllegalArgumentException ex) {
        return Optional.empty();
    }
  }
}
