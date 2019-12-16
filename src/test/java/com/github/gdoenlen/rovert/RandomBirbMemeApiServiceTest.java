package com.github.gdoenlen.rovert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RandomBirbMemeApiServiceTest {

    @Test
    void get_should_return_an_empty_optional_when_the_service_fails() {
        var url = "https://localhost:8080/";
        var client = Mockito.mock(HttpClient.class);
        var api = new RandomBirbMemeApiService(url, client);
        var response = Mockito.mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(500);

        @SuppressWarnings("unchecked")
        CompletableFuture<HttpResponse<String>> cf = CompletableFuture.completedFuture(response);
        when(client.sendAsync(any(), eq(BodyHandlers.ofString()))).thenReturn(cf);

        CompletableFuture<Optional<URI>> shouldBeEmpty = api.get();
        assertTrue(shouldBeEmpty.join().isEmpty());
    }

    @Test
    void get_should_return_the_uri_when_the_service_succeeds() {
        var url = "https://localhost:8080/";
        var client = Mockito.mock(HttpClient.class);
        var api = new RandomBirbMemeApiService(url, client);
        var response = Mockito.mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("123.jpg");

        @SuppressWarnings("unchecked")
        CompletableFuture<HttpResponse<String>> cf = CompletableFuture.completedFuture(response);
        when(client.sendAsync(any(), eq(BodyHandlers.ofString()))).thenReturn(cf);

        CompletableFuture<Optional<URI>> shouldNotBeEmpty = api.get();
        var uri = shouldNotBeEmpty.join();
        assertTrue(uri.isPresent());
        assertEquals(URI.create(url + "img/123.jpg"), uri.get());
    }
}
