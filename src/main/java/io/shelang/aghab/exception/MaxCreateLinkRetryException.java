package io.shelang.aghab.exception;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;

public class MaxCreateLinkRetryException extends InternalServerErrorException {
    private static final String DEFAULT_MESSAGE = "Could not create link due to retrying for generating short link";

    public MaxCreateLinkRetryException() {
        super(DEFAULT_MESSAGE);
    }

    public MaxCreateLinkRetryException(String message) {
        super(message);
    }

    public MaxCreateLinkRetryException(Response response) {
        super(response);
    }

    public MaxCreateLinkRetryException(String message, Response response) {
        super(message, response);
    }

    public MaxCreateLinkRetryException(Throwable cause) {
        super(cause);
    }

    public MaxCreateLinkRetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaxCreateLinkRetryException(Response response, Throwable cause) {
        super(response, cause);
    }

    public MaxCreateLinkRetryException(String message, Response response, Throwable cause) {
        super(message, response, cause);
    }
}
