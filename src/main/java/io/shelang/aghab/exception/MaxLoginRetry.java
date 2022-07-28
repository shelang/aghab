package io.shelang.aghab.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class MaxLoginRetry extends WebApplicationException {
  private static final String DEFAULT_MESSAGE =
      "You Entered your password wrong, you banned for at least 2 hours.";

  public MaxLoginRetry() {
    super(DEFAULT_MESSAGE, Response.Status.NOT_ACCEPTABLE);
  }
}
