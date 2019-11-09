package com.github.gdoenlen.rovert;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Model that represents the standard event wrapper from slack
 */
public class Event {
    @JsonProperty("api_app_id")
    private String apiAppId;

    @JsonProperty("authed_users")
    private List<String> authedUsers;

    @JsonProperty
    private String challenge;

    @JsonProperty
    private JsonNode event;

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("event_time")
    private Long eventTime;

    @JsonProperty("team_id")
    private String teamId;

    @JsonProperty
    private String token;

    @JsonProperty
    private String type;

    public String getApiAppId() {
        return this.apiAppId;
    }

    public void setApiAppId(String apiAppId) {
        this.apiAppId = apiAppId;
    }

    public List<String> getAuthedUsers() {
        return this.authedUsers;
    }

    public void setAuthedUsers(List<String> authedUsers) {
        this.authedUsers = authedUsers;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public JsonNode getEvent() {
        return event;
    }

    public void setEvent(JsonNode event) {
        this.event = event;
    }

    public String getEventId() {
        return this.eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getEventTime() {
        return this.eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    public String getTeamId() {
        return this.teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
