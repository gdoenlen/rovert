package com.github.gdoenlen.rovert.producers;

import java.net.http.HttpClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

class HttpProducer {

    @Produces
    @ApplicationScoped
    HttpClient produceHttpClient() {
        return HttpClient.newHttpClient();
    }
}