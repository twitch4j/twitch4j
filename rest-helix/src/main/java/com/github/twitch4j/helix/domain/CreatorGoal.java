package com.github.twitch4j.helix.domain;

import com.github.twitch4j.eventsub.domain.GoalType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class CreatorGoal {

    /**
     * An ID that uniquely identifies this goal.
     */
    private String id;

    /**
     * An ID that uniquely identifies the broadcaster.
     */
    private String broadcasterId;

    /**
     * The broadcaster’s display name.
     */
    private String broadcasterName;

    /**
     * The broadcaster’s user handle.
     */
    private String broadcasterLogin;

    /**
     * The type of goal.
     */
    private GoalType type;

    /**
     * A description of the goal, if specified.
     * <p>
     * The description may contain a maximum of 40 characters.
     */
    private String description;

    /**
     * The current value.
     * <p>
     * If the goal is to increase followers, this field is set to the current number of followers.
     * This number increases with new followers and decreases if users unfollow the channel.
     * <p>
     * For subscriptions, current_amount is increased and decreased by the points value associated with the subscription tier.
     * For example, if a tier-two subscription is worth 2 points, current_amount is increased or decreased by 2, not 1.
     */
    private Integer currentAmount;

    /**
     * The goal’s target value.
     * <p>
     * For example, if the broadcaster has 200 followers before creating the goal, and their goal is to double that number, this field is set to 400.
     */
    private Integer targetAmount;

    /**
     * The UTC timestamp in RFC 3339 format, which indicates when the broadcaster created the goal.
     */
    private Instant createdAt;

}
