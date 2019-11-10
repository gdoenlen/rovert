package com.github.gdoenlen.rovert;

import java.io.UncheckedIOException;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/** Handler for UncheckedIOExceptions */
@ApplicationScoped
public class UncheckedIOExceptionMapper implements ExceptionMapper<UncheckedIOException> {

    @Override
    public Response toResponse(UncheckedIOException exception) {
        return Response.serverError().build();
    }
}