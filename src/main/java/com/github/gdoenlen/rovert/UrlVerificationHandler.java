package com.github.gdoenlen.rovert;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Handler for the initial "url_challenge" event which slack sends once
 * you register your url.
 * 
 * Its only goal is to return the challenge value from event.
 */
@ApplicationScoped
public class UrlVerificationHandler implements EventHandler {

    @Override
    public Response handle(Event event) {
        Objects.requireNonNull(event);

        return Response.ok(event.getChallenge()).header(Headers.CONTENT_TYPE, MediaType.TEXT_PLAIN).build();
    }

    @Override
    public String getEventType() {
        return "url_verification";
    }
}