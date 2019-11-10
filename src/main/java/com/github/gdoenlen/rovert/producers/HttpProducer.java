package com.github.gdoenlen.rovert.producers;

import java.net.http.HttpClient;

import javax.enterprise.inject.Produces;

class HttpProducer {

    @Produces
    HttpClient produceHttpClient() {
        return HttpClient.newHttpClient();
    }
}