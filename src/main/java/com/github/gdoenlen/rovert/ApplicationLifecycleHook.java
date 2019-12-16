package com.github.gdoenlen.rovert;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

/**
 * Hooks for the quarkus runtime.
 */
@ApplicationScoped
public class ApplicationLifecycleHook {

    private final ScheduledExecutorService scheduler;
    private final RandomBirbMemeApiService birb;
    private final SlackWebApiService slack;
    private final String channel;

    @Inject
    public ApplicationLifecycleHook(
        ScheduledExecutorService scheduler,
        RandomBirbMemeApiService birb,
        SlackWebApiService slack,
        @ConfigProperty(name = "rovert.birb.channel") String channel
    ) {
        Objects.requireNonNull(scheduler);
        Objects.requireNonNull(birb);
        Objects.requireNonNull(slack);
        Objects.requireNonNull(channel);

        this.scheduler = scheduler;
        this.birb = birb;
        this.slack = slack;
        this.channel = channel;
    }

    /**
     * Hook for the quarkus start up event. Will
     * immediately post a birb meme to the rovert.birb.channel
     * and then will schedule itself to do the same 1 day
     * later.
     *
     * @param ev The startup event
     */
    public void onStart(@Observes StartupEvent ev) {
        this.postBirbMeme();
    }

    /**
     * Hook for the quarkus shut down event.
     *
     * Currently only shut's down the scheduler.
     *
     * The scheduler is currently used for:
     * 1. Setting the guard to the delay mechanism in MessageHandler.java.
     * 2. Posting the birb meme every 24 hours.
     *
     * shutDownNow is appropriate because we don't care if that guard gets reset
     * after the cooldown because the application won't be receiving any more
     * requests and posting the birb meme doesn't matter if the bot is down.
     *
     * @param ev the shutdown event
     */
    public void onStop(@Observes ShutdownEvent ev) {
        this.scheduler.shutdownNow();
    }

    private void postBirbMeme() {
        this.birb.get()
            .thenAccept(uri -> uri.ifPresent(u -> this.slack.postMessage(this.channel, u.toString())))
            .thenRun(() -> this.scheduler.schedule(this::postBirbMeme, 1L, TimeUnit.DAYS));
    }
}
