package com.github.gdoenlen.rovert;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

class ApplicationLifecycleHookTest {

    @Test
    void it_should_shutdown_the_scheduler() {
        var scheduler = Mockito.mock(ScheduledExecutorService.class);
        var birb = Mockito.mock(RandomBirbMemeApiService.class);
        var slack = Mockito.mock(SlackWebApiService.class);
        var hooks = new ApplicationLifecycleHook(scheduler, birb, slack, "channel");
        var shutdownEvent = Mockito.mock(ShutdownEvent.class);
        hooks.onStop(shutdownEvent);

        verify(scheduler).shutdownNow();
    }

    @Test
    void it_should_post_a_birb_meme_on_start_and_schedule_it_for_a_day_later() {
        var scheduler = Mockito.mock(ScheduledExecutorService.class);
        var birb = Mockito.mock(RandomBirbMemeApiService.class);
        var slack = Mockito.mock(SlackWebApiService.class);
        var channel = "channel";
        var hooks = new ApplicationLifecycleHook(scheduler, birb, slack, channel);
        var startupEvent = Mockito.mock(StartupEvent.class);

        var uri = URI.create("https://localhost:8080/img/123.jpg");
        when(birb.get()).thenReturn(CompletableFuture.completedFuture(Optional.of(uri)));
        hooks.onStart(startupEvent);

        verify(slack).postMessage(channel, uri.toString());
        verify(scheduler).schedule(any(Runnable.class), eq(1L), eq(TimeUnit.DAYS));
    }

    @Test
    void it_should_not_post_to_slack_if_the_birb_service_fails_and_it_should_schedule_itself() {
        var scheduler = Mockito.mock(ScheduledExecutorService.class);
        var birb = Mockito.mock(RandomBirbMemeApiService.class);
        var slack = Mockito.mock(SlackWebApiService.class);
        var channel = "channel";
        var hooks = new ApplicationLifecycleHook(scheduler, birb, slack, channel);
        var startupEvent = Mockito.mock(StartupEvent.class);

        when(birb.get()).thenReturn(CompletableFuture.completedFuture(Optional.empty()));
        hooks.onStart(startupEvent);

        verify(slack, times(0)).postMessage(any(), any());
        verify(scheduler).schedule(any(Runnable.class), eq(1L), eq(TimeUnit.DAYS));
    }
}
