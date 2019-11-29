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
        // The scheduler is currently only used for setting
        // the guard to the delay mechanism in MessageHandler.java.
        // shutDownNow is appropriate because we don't care
        // if that guard gets reset after the cooldown because the
        // application won't be receiving any more requests.
        this.scheduler.shutdownNow();
    }        
}
