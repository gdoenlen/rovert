package com.github.gdoenlen.rovert;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import com.github.gdoenlen.rovert.EventHandlerFactory.NoHandlerFoundException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EventHandlerFactoryTest {
    @Test
    void it_should_return_the_handler_matching_the_type() {
        var handler = new UrlVerificationHandler();
        var messageHandler = Mockito.mock(MessageHandler.class);
        when(messageHandler.getEventType()).thenReturn("doesn'tmatter");
        var factory = new EventHandlerFactory(handler, messageHandler);
        assertTrue(handler == factory.get(handler.getEventType()));
    }

    @Test
    void it_should_throw_NoHandlerFoundException_when_there_is_no_handler() {
        var handler = new UrlVerificationHandler();
        var messageHandler = Mockito.mock(MessageHandler.class);
        when(messageHandler.getEventType()).thenReturn("doesn'tmatter");
        var factory = new EventHandlerFactory(handler, messageHandler);
        try {
            factory.get("hello");
        } catch (NoHandlerFoundException ex) {
            assertTrue(true);
            return;
        }
        fail();
    }
}
