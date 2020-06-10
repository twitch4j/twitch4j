package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.pubsub.domain.RedemptionProgress;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UpdateRedemptionProgressEvent extends UpdateRedemptionStatusesEvent {
    public UpdateRedemptionProgressEvent(Instant timestamp, RedemptionProgress progress) {
        super(timestamp, progress);
    }
}
