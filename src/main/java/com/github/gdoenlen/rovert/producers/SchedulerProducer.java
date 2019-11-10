package com.github.gdoenlen.rovert.producers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.enterprise.inject.Produces;

/**
 * Executor services for scheduling threads
 */
class SchedulerProducer {

    @Produces
    ScheduledExecutorService produceScheduledExecutorService() {
        return Executors.newScheduledThreadPool(1);   
    }
}
