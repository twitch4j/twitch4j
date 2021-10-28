package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.CreatorGoal;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class CreatorGoalEvent extends TwitchEvent {

    String channelId;

    String type;

    CreatorGoal goal;

    public boolean isGoalCreate() {
        return "goal_created".equalsIgnoreCase(type);
    }

    public boolean isGoalUpdate() {
        return "goal_updated".equalsIgnoreCase(type);
    }

    public boolean isGoalAchieve() {
        return "goal_achieved".equalsIgnoreCase(type);
    }

    public boolean isGoalFinish() {
        return "goal_finished".equalsIgnoreCase(type);
    }

}
