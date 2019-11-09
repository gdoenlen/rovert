package com.github.gdoenlen.rovert;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.github.gdoenlen.rovert.EventHandlerFactory.NoHandlerFoundException;

@Provider
public class NoHandlerFoundExceptionMapper implements ExceptionMapper<EventHandlerFactory.NoHandlerFoundException> {

    @Override
    public Response toResponse(NoHandlerFoundException exception) {
        return Response.status(Status.BAD_REQUEST)
            .entity(exception.getMessage())
            .build();
    }
}
