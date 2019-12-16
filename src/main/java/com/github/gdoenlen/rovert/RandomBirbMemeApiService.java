package com.github.gdoenlen.rovert;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Service to get a random birb meme whenever `get` is called.
 * Currently it only gets it from a single api endpoint, but
 * eventually this should add some support for other sources.
 */
@ApplicationScoped
public class RandomBirbMemeApiService {
    private final URI apiUri;
    private final String imgUri;
    private final HttpClient http;

    /**
     * @param url the base url to the the api
     * @param http standard http client
     */
    public RandomBirbMemeApiService(@ConfigProperty(name = "rovert.birb.url") String url, HttpClient http) {
        Objects.requireNonNull(url);
        Objects.requireNonNull(http);

        this.apiUri = URI.create(url + "tweet");
        this.imgUri = url + "img/";
        this.http = http;
    }

    /**
     * Gets URI to a random bird meme.
     * 
     * @return A future that with the uri to the meme or empty
     *         if the request fails.
     */
    public CompletableFuture<Optional<URI>> get() {
        var request = HttpRequest.newBuilder(this.apiUri)
            .GET()
            .build();

        return this.http.sendAsync(request, BodyHandlers.ofString()).thenApply(response ->  {
            if (response.statusCode() != Response.Status.OK.getStatusCode()) {
                return Optional.empty();
            }
            return Optional.of(URI.create(this.imgUri + response.body()));
        });
    }
}
