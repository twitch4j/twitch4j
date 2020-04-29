package com.github.twitch4j.helix.webhooks.topics;

import com.github.twitch4j.helix.domain.SubscriptionList;
import lombok.Getter;
import lombok.NonNull;

import java.util.Optional;
import java.util.TreeMap;

/**
 * This webhook notifies you when:
 * <ul>
 *     <li>A payment has been processed for a subscription or unsubscription.
 *     <li>A user who is subscribed to a broadcaster notifies the broadcaster of their subscription in the chat.
 * </ul>
 */
@Getter
public class ChannelSubscriptionTopic extends TwitchWebhookTopic<SubscriptionList> {

    public static final String PATH = "/subscriptions/events";

    private static TreeMap<String, Object> mapParameters(String broadcasterId, String userId) {
        TreeMap<String, Object> parameterMap = new TreeMap<>();
        parameterMap.put("broadcaster_id", broadcasterId);
        parameterMap.put("first", 1);
        parameterMap.put("user_id", userId);
        return parameterMap;
    }

    /**
     * @return The user ID of the broadcaster.
     */
    private String broadcasterId;

     /**
     * @return The ID of the subscribed user.
     */
    private Optional<String> userId;

    /**
     * This webhook notifies you when:
     * <ul>
     *     <li>A payment has been processed for a subscription or unsubscription.
     *     <li>A user who is subscribed to a broadcaster notifies the broadcaster of their subscription in the chat.
     * </ul>
     *
     * @param broadcasterId Required. User ID of the broadcaster. Must match the User ID in the Bearer token.
     * @param userId Optional. ID of the subscribed user. Currently only one user_id at a time can be queried.
     */
    public ChannelSubscriptionTopic(@NonNull String broadcasterId, String userId) {
        super(
            PATH,
            SubscriptionList.class,
            mapParameters(broadcasterId, userId)
        );
        this.broadcasterId = broadcasterId;
        this.userId = Optional.ofNullable(userId);

    }

}
