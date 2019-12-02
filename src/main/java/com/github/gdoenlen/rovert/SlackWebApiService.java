package com.github.gdoenlen.rovert;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Services for Slack's web api.
 * Slacks api is usually noun.verb in their naming conventions,
 * in this service we do the opposite, verbNoun. 
 * 
 * (e.g. reactions.add is addReaction)
 * 
 * Only the part of the api we need is implemented. 
 */
@ApplicationScoped
public class SlackWebApiService {

    static final String ADD_REACTION_FORMAT_STRING = "/reaction.add?channel=%s&name=%s&timestamp=%s&token=%s";

    private final HttpClient client;
    private final String url;
    private final String token;

    /**
     * @param client the http client to do http calls
     * @param url The url to the slack api, this should not end with a /
     * @param mapper mapper to deserialize responses
     * @param token Slack api token from the app management page
     */
    @Inject
    public SlackWebApiService(
        HttpClient client,
        @ConfigProperty(name = "slack.api.url") String url,
        @ConfigProperty(name = "slack.api.token") String token
    ) {
        Objects.requireNonNull(client);
        Objects.requireNonNull(url);
        Objects.requireNonNull(token);

        this.client = client;
        this.url = url;
        this.token = token;
    }
    
    /**
     * Asynchronously adds a reaction to a message in a specific channel
     * 
     * @param channel The channel the message was in
     * @param name the name of the reaction to apply
     * @param timestamp the timestamp of when the message occured
     * @return The response from the api whenever it returns.
     */
    public CompletableFuture<HttpResponse<Void>> addReaction(
        String channel,
        String name,
        String timestamp
    ) {
        Objects.requireNonNull(channel);
        Objects.requireNonNull(name);
        Objects.requireNonNull(timestamp);

        var request = HttpRequest.newBuilder()
            .uri(URI.create(String.format(
                this.url + ADD_REACTION_FORMAT_STRING,
                channel,
                name,
                timestamp,
                this.token
            )))
            .header(Headers.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)
            .POST(BodyPublishers.noBody())
            .build();
        
        return this.client.sendAsync(request, BodyHandlers.discarding());
    }
}
