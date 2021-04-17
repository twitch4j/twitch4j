package com.github.twitch4j.pubsub.domain;

import lombok.Data;

@Data
public class CreateNotificationData {
    private NotificationSummary summary;
    private OnsiteNotification notification;
    private Boolean persistent;
    private Boolean toast;
}
