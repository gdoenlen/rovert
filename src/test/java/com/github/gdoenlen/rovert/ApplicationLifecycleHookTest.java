package com.github.gdoenlen.rovert;

import static org.mockito.Mockito.verify;

import java.util.concurrent.ScheduledExecutorService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.runtime.ShutdownEvent;

class ApplicationLifecycleHookTest {

    @Test
    void it_should_shutdown_the_scheduler() {
        var scheduler = Mockito.mock(ScheduledExecutorService.class);
        var hooks = new ApplicationLifecycleHook(scheduler);
        var shutdownEvent = Mockito.mock(ShutdownEvent.class);
        hooks.onStop(shutdownEvent);

        verify(scheduler).shutdownNow();
    }
}