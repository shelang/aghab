package io.shelang.aghab.event;

public final class EventType {

  private EventType() throws IllegalAccessException {
    throw new IllegalAccessException();
  }

  public static final String ANALYTIC_LINK = "analytic-link";
  public static final String WEBHOOK_CALL = "webhook-call";

}
