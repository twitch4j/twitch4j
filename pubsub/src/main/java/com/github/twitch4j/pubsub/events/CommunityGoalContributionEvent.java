package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.CommunityGoalContribution;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommunityGoalContributionEvent extends TwitchEvent {
    private final Instant timestamp;
    private final CommunityGoalContribution contribution;
}
