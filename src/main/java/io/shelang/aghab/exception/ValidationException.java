package io.shelang.aghab.exception;

import io.shelang.aghab.service.dto.link.LinkCreateDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

import java.util.Set;

@Getter
public class ValidationException extends WebApplicationException {

    private static final String DEFAULT_MESSAGE =
            "Constraint Violation";

    private final Set<ConstraintViolation<LinkCreateDTO>> violations;

    public ValidationException(Set<ConstraintViolation<LinkCreateDTO>> violations) {
        super(DEFAULT_MESSAGE, Response.Status.BAD_REQUEST);
        this.violations = violations;
    }

}
