package io.shelang.aghab.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class MaxCreateLinkRetryException extends WebApplicationException {
  private static final String DEFAULT_MESSAGE =
      "Could not create link due to retrying for generating short link";

  public MaxCreateLinkRetryException() {
    super(DEFAULT_MESSAGE, Response.Status.NOT_ACCEPTABLE);
  }
}
