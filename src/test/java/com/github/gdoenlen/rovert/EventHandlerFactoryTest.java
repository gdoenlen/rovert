package com.github.gdoenlen.rovert;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.gdoenlen.rovert.EventHandlerFactory.NoHandlerFoundException;

import org.junit.jupiter.api.Test;

class EventHandlerFactoryTest {
    @Test
    void it_should_return_the_handler_matching_the_type() {
        var handler = new UrlVerificationHandler();
        var factory = new EventHandlerFactory(handler);
        assertTrue(handler == factory.get(handler.getEventType()));
    }

    @Test
    void it_should_throw_NoHandlerFoundException_when_there_is_no_handler() {
        var handler = new UrlVerificationHandler();
        var factory = new EventHandlerFactory(handler);
        try {
            factory.get("hello");
        } catch (NoHandlerFoundException ex) {
            assertTrue(true);
            return;
        }
        fail();
    }
}
