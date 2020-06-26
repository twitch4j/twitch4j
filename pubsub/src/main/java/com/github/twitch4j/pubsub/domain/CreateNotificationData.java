package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateNotificationData {
    private OnsiteNotification notification;
    private Boolean persistent;
    private Boolean toast;
}
