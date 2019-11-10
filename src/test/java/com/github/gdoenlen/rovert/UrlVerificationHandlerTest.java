package com.github.gdoenlen.rovert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UrlVerificationHandlerTest {

    @Test
    void get_event_type_should_be_url_verification() {
        var handler = new UrlVerificationHandler();
        assertEquals("url_verification", handler.getEventType());
    }

    @Test
    void handle_should_return_ok_with_the_challenge_value() {
        var handler = new UrlVerificationHandler();
        var event = Mockito.mock(Event.class);
        var challenge = "12345";
        when(event.getChallenge()).thenReturn(challenge);

        Response response = handler.handle(event);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertEquals(challenge, response.getEntity().toString());
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(Headers.CONTENT_TYPE));
    }
}
