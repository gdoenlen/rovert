package com.github.gdoenlen.rovert;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import com.github.gdoenlen.rovert.Event.SubEvent;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Event handler for the the slack "message" type of event.
 * 
 * @see https://api.slack.com/events/message.channels
 */
@ApplicationScoped
public class MessageHandler implements EventHandler {
    
    private final SlackWebApiService slack;
    private final String react;
    private final String userId;
    private final long delay;
    private final ScheduledExecutorService scheduler;
    private final AtomicBoolean sendMessage = new AtomicBoolean(true);

    /**
     * @param slack the slack api 
     * @param react the emoji to react to the message with
     * @param userId the user to react to
     * @param delay how often the user should be reacted to in minutes.
     */
    @Inject
    public MessageHandler(
        SlackWebApiService slack,
        ScheduledExecutorService scheduler,
        @ConfigProperty(name = "rovert.messages.reaction") String react,
        @ConfigProperty(name = "rovert.messages.user") String userId,
        @ConfigProperty(name = "rovert.messages.delay") long delay
    ) {
        Objects.requireNonNull(slack);
        Objects.requireNonNull(scheduler);
        Objects.requireNonNull(react);
        Objects.requireNonNull(userId);

        this.slack = slack;
        this.scheduler = scheduler;
        this.react = react;
        this.userId = userId;
        this.delay = delay;
    }
    
    @Override
    public String getEventType() {
        return "message.channels";
    }

    /**
     * Handlers the incoming event.
     * It will react to only the user specified and if not in a cooldown period
     * 
     * @param event the incoming event from slack
     * @return always noContent (204)
     */
    @Override
    public Response handle(Event event) {
        Objects.requireNonNull(event);

        SubEvent inner = event.getEvent();
        String timestamp = inner.getEventTimestamp();
        String user = inner.getUser();
        String channel = inner.getChannel();
        
        if (userId.equals(user) && sendMessage.compareAndSet(true, false)) {
            this.slack.addReaction(channel, this.react, timestamp);

            if (this.delay > 0) {
                this.scheduler.schedule(() -> sendMessage.set(true), this.delay, TimeUnit.MINUTES);
            } else {
                sendMessage.set(true);
            }
        }
        
        return Response.noContent().build();
    }
}
