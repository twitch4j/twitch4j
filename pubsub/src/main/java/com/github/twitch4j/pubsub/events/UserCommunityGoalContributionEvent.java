package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.CommunityGoalContribution;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserCommunityGoalContributionEvent extends TwitchEvent {
    String userId;
    Instant timestamp;
    CommunityGoalContribution contribution;
}
