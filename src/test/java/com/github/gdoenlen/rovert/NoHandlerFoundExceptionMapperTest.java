package com.github.gdoenlen.rovert;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Test;

class NoHandlerFoundExceptionMapperTest {
    @Test
    void it_should_respond_with_bad_request_and_the_exception_message() {
        var mapper = new NoHandlerFoundExceptionMapper();
        var message = "doesn'tmatter";
        var ex = new EventHandlerFactory.NoHandlerFoundException(message);
        Response response = mapper.toResponse(ex);
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals(message, response.getEntity().toString());
    }
}
