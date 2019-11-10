package com.github.gdoenlen.rovert;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controller that handles slack event api requests.
 */
@ApplicationScoped
@Path("/events")
@Consumes(MediaType.APPLICATION_JSON)
public class EventsController {
    private final EventHandlerFactory handlers;

    @Inject
    public EventsController(EventHandlerFactory handlers) {
        Objects.requireNonNull(handlers);

        this.handlers = handlers;
    }
    
    /**
     * Post endpoint for /events.
     * 
     * Every event should be sent through
     * this endpoint from slack. It will forward the event to any registered
     * handler in the factory.
     * 
     * @param event the incoming event from slack
     * @return the response from the handler or bad request if no handler is found.
     */
    @POST
    public Response post(Event event) {
        Objects.requireNonNull(event);        

        String type = event.getType();
        if ("event_callback".equals(type)) {
            type = event.getEvent().get("type").asText();
        }

        return handlers.get(event.getType()).handle(event);
    }
}
