package com.github.twitch4j.helix.webhooks.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public class WebhookNotification {

    private Map<String, Object> data;

}
