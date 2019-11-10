package com.github.gdoenlen.rovert;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Test;

class UncheckedIOExceptionMapperTest {
    @Test
    void it_should_respond_with_server_error() {
        var mapper = new UncheckedIOExceptionMapper();
        Response response = mapper.toResponse(null);
        assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }
}