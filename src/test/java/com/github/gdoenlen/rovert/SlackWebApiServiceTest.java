package com.github.gdoenlen.rovert;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SlackWebApiServiceTest {

    @Test
    void it_should_should_post_to_the_api_with_the_reaction_data() {
        var client = Mockito.mock(HttpClient.class);
        var url = "https://github.com/gdoenlen";
        var token = "token";
        var channel = "channel";
        var name = "name";
        var timestamp = "timestamp";
        var request = HttpRequest.newBuilder()
            .uri(URI.create(String.format(
                url + SlackWebApiService.ADD_REACTION_FORMAT_STRING,
                channel,
                name,
                timestamp,
                token
            )))
            .header(Headers.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)
            .POST(BodyPublishers.noBody())
            .build();
        
        @SuppressWarnings("unchecked")
        HttpResponse<Object> response = Mockito.mock(HttpResponse.class);
        when(client.sendAsync(any(), any())).thenReturn(CompletableFuture.completedFuture(response));

        var service = new SlackWebApiService(client, url, token);
        service.addReaction(channel, name, timestamp);
        verify(client).sendAsync(request, BodyHandlers.discarding());
    }
}
