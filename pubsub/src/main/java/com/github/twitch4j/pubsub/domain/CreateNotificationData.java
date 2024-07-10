package com.github.twitch4j.pubsub.domain;

import lombok.Data;
import lombok.Setter;

@Data
@Setter(onMethod_ = { @Deprecated })
public class CreateNotificationData {
    private NotificationSummary summary;
    private OnsiteNotification notification;
    private Boolean persistent;
    private Boolean toast;
}
