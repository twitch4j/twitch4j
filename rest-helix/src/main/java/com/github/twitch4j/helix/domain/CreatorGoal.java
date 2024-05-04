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
     * If type is {@link GoalType#FOLLOWERS}, this field is set to the broadcaster's current number of followers.
     * This number increases with new followers and decreases when users unfollow the broadcaster.
     * <p>
     * If type is {@link GoalType#SUBSCRIPTIONS}, this field is increased and decreased by the points value associated with the subscription tier.
     * For example, if a tier-two subscription is worth 2 points, this field is increased or decreased by 2, not 1.
     * <p>
     * If type is {@link GoalType#SUB_COUNT}, this field is increased by 1 for each new subscription and decreased by 1 for each user that unsubscribes.
     * <p>
     * If type is {@link GoalType#NEW_SUBSCRIPTIONS}, this field is increased by the points value associated with the subscription tier.
     * For example, if a tier-two subscription is worth 2 points, this field is increased by 2, not 1.
     * <p>
     * If type is {@link GoalType#NEW_SUB_COUNT}, this field is increased by 1 for each new subscription.
     * <p>
     * If type is {@link GoalType#BITS}, this field is increased by the number of bits cheered.
     * <p>
     * If type is {@link GoalType#CHEERS}, this field is increased by 1 for each new cheer.
     *
     * @see #getType()
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
