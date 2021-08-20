package com.github.twitch4j.helix.webhooks.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
@Deprecated
public class WebhookNotification {

    private Map<String, Object> data;

}
