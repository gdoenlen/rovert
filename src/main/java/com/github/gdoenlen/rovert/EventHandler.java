package com.github.gdoenlen.rovert;

import javax.ws.rs.core.Response;

/**
 * A simple interface used to handle the event requests coming from slack.
 * Since the slack api (and therefore this app) uses one single post endpoint
 * for the api calls there needed to be a simple way to represent all of the different
 * types of Responses that could come back from different event types.
 */
public interface EventHandler {
    String getEventType();
    Response handle(Event event);
}
