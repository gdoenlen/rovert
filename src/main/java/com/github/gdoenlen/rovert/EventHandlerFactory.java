package com.github.gdoenlen.rovert;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Factory for getting the handlers for the slack events. 
 */
@ApplicationScoped
public class EventHandlerFactory {
    
    private final Map<String, EventHandler> handlers = new HashMap<String, EventHandler>();

    @Inject
    public EventHandlerFactory(UrlVerificationHandler urlVerificationHandler) {
        this.handlers.put(urlVerificationHandler.getEventType(), urlVerificationHandler);
    }

    /**
     * Gets the event handler for the given type.
     * 
     * @param type the type of the event from the slap api
     * @return the handler registered for the type
     * @throws NoHandlerFoundException when no handler is registered for that type
     */
    public EventHandler get(String type) {
        EventHandler handler = handlers.get(type);
        if (handler == null) {
            throw new NoHandlerFoundException("No event handler found for type: " + type);
        }
        return handler;
    }

    public class NoHandlerFoundException extends RuntimeException {
        private static final long serialVersionUID = 3905558720764253870L;

        public NoHandlerFoundException(String message) {
            super(message);
        }
    }
}