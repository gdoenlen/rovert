package com.github.gdoenlen.rovert.producers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Executor services for scheduling threads
 */
class SchedulerProducer {

    @Produces
    @ApplicationScoped
    ScheduledExecutorService produceScheduledExecutorService() {
        return Executors.newScheduledThreadPool(1);   
    }
}
