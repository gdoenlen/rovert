package com.github.gdoenlen.rovert;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/** Handler for UncheckedIOExceptions */
@ApplicationScoped
public class UncheckedIOExceptionMapper implements ExceptionMapper<IOException> {

    @Override
    public Response toResponse(IOException exception) {
        return Response.serverError().build();
    }
}