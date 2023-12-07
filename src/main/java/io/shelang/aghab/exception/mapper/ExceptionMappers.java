package io.shelang.aghab.exception.mapper;

import io.shelang.aghab.exception.ValidationException;
import io.shelang.aghab.service.dto.link.LinkCreateDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.ArrayList;

public class ExceptionMappers {

    @ServerExceptionMapper
    public Response WebApplicationExceptionMapper(WebApplicationException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse()
                .setStatus(e.getResponse().getStatus())
                .setTitle(e.getMessage());
        return Response.status(e.getResponse().getStatus())
                .entity(exceptionResponse)
                .build();
    }

    @ServerExceptionMapper
    public Response ValidationExceptionMapper(ValidationException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse()
                .setStatus(e.getResponse().getStatus())
                .setTitle(e.getMessage())
                .setViolations(new ArrayList<>(e.getViolations().size()));

        for (ConstraintViolation<LinkCreateDTO> v : e.getViolations()) {
            exceptionResponse
                    .getViolations()
                    .add(new ViolationError()
                            .setField(v.getPropertyPath().toString())
                            .setMessage(v.getMessage()));
        }

        return Response.status(e.getResponse().getStatus())
                .entity(exceptionResponse)
                .build();
    }

}
