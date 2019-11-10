package com.github.gdoenlen.rovert;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.github.gdoenlen.rovert.EventHandlerFactory.NoHandlerFoundException;

/**
 * Exception mapper to handle exceptions thrown from `EventHandlerFactory`
 * when a handler isn't found for a requested type. 
 * 
 * Always returns bad request.
 */
@Provider
public class NoHandlerFoundExceptionMapper implements ExceptionMapper<EventHandlerFactory.NoHandlerFoundException> {

    @Override
    public Response toResponse(NoHandlerFoundException exception) {
        return Response.status(Status.BAD_REQUEST)
            .entity(exception.getMessage())
            .build();
    }
}
