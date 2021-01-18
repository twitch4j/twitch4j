package com.github.twitch4j.helix.webhooks.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
public class WebhookNotification {

    @JsonProperty("data")
    private Map<String, Object> data;

}
