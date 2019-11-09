package com.github.gdoenlen.rovert;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    
    @POST
    public Response post(Event event) {
        Objects.requireNonNull(event);        

        return handlers.get(event.getType()).handle(event);
    }
}
