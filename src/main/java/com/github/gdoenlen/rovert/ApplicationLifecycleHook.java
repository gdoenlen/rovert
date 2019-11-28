package com.github.gdoenlen.rovert;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import io.quarkus.runtime.ShutdownEvent;

/**
 * Hooks for the quarkus runtime.
 */
@ApplicationScoped
public class ApplicationLifecycleHook {

    private final ScheduledExecutorService scheduler;

    @Inject
    public ApplicationLifecycleHook(ScheduledExecutorService scheduler) {
        Objects.requireNonNull(scheduler);

        this.scheduler = scheduler;
    }
    
    public void onStop(@Observes ShutdownEvent ev) {
        this.scheduler.shutdown();
    }        
}
