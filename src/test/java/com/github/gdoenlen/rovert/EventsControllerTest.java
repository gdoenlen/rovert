package com.github.gdoenlen.rovert;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EventsControllerTest {

    @Test
    void it_should_return_the_found_handlers_response() {
        var factory = Mockito.mock(EventHandlerFactory.class);
        var handler = Mockito.mock(EventHandler.class);
        var event = new Event();
        event.setType("doesn'tmatter");

        var response = Response.ok().build();
        when(factory.get(anyString())).thenReturn(handler);
        when(handler.handle(event)).thenReturn(response);

        var controller = new EventsController(factory);
        Response r = controller.post(event);
        assertTrue(r == response);
    }

    @Test
    void it_should_use_the_inner_events_type_if_type_is_event_callback() {
        var factory = Mockito.mock(EventHandlerFactory.class);
        var handler = Mockito.mock(EventHandler.class);
        var event = new Event();
        var inner = new Event.SubEvent();
        inner.setType("doesn'tmatter");
        event.setType("event_callback");
        event.setEvent(inner);

        when(factory.get(anyString())).thenReturn(handler);

        var controller = new EventsController(factory);
        controller.post(event);

        verify(factory).get(inner.getType());
    }
}