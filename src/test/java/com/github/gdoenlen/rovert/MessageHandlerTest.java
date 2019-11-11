package com.github.gdoenlen.rovert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.concurrent.ScheduledExecutorService;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MessageHandlerTest {

    @Test
    void it_should_throw_illegal_argument_exception_if_delay_is_less_than_zero() {
        try {
            var slack = Mockito.mock(SlackWebApiService.class);
            var scheduler = Mockito.mock(ScheduledExecutorService.class);
            var handler = new MessageHandler(slack, scheduler, "", "", 0);
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void it_should_return_no_content() {
        var event = new Event();
        var inner = new Event.SubEvent();
        event.setEvent(inner);

        var user = "rovert";
        var timestamp = "timestamp";
        var channel = "channel";
        inner.setUser(user);
        inner.setEventTimestamp(timestamp);
        inner.setChannel(channel);

        var slack = Mockito.mock(SlackWebApiService.class);
        var scheduler = Mockito.mock(ScheduledExecutorService.class);
        var handler = new MessageHandler(slack, scheduler, "", user, 1L);

        Response response = handler.handle(event);
        assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }
    
    @Test
    void it_should_add_reaction_if_the_user_is_the_same() {
        var event = new Event();
        var inner = new Event.SubEvent();
        event.setEvent(inner);

        var user = "rovert";
        var timestamp = "timestamp";
        var channel = "channel";
        inner.setUser(user);
        inner.setEventTimestamp(timestamp);
        inner.setChannel(channel);

        var slack = Mockito.mock(SlackWebApiService.class);
        var scheduler = Mockito.mock(ScheduledExecutorService.class);
        var delay = 1L;
        var handler = new MessageHandler(slack, scheduler, "", user, delay);

        Response response = handler.handle(event);

        verify(slack).addReaction(channel, "", timestamp);
    }

    @Test
    void it_should_not_add_reaction_if_the_user_is_not_the_same() {
        var event = new Event();
        var inner = new Event.SubEvent();
        event.setEvent(inner);

        var user = "rovert";
        var timestamp = "timestamp";
        var channel = "channel";
        inner.setUser(user);
        inner.setEventTimestamp(timestamp);
        inner.setChannel(channel);

        var slack = Mockito.mock(SlackWebApiService.class);
        var scheduler = Mockito.mock(ScheduledExecutorService.class);
        var delay = 1L;
        var handler = new MessageHandler(slack, scheduler, "", "bob", delay);

        Response response = handler.handle(event);

        verify(slack, never()).addReaction(channel, "", timestamp);
    }
}
