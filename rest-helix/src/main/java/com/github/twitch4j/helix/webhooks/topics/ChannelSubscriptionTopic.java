package com.github.twitch4j.helix.webhooks.topics;

import com.github.twitch4j.helix.domain.SubscriptionEventList;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.TreeMap;

/**
 * This webhook notifies you when:
 * <ul>
 *     <li>A payment has been processed for a subscription or unsubscription.
 *     <li>A user who is subscribed to a broadcaster notifies the broadcaster of their subscription in the chat.
 * </ul>
 *
 * @deprecated Twitch decommissioned this API; please migrate to EventSub
 */
@Getter
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public class ChannelSubscriptionTopic extends TwitchWebhookTopic<SubscriptionEventList> {

    public static final String PATH = "/subscriptions/events";

    private static TreeMap<String, Object> mapParameters(String broadcasterId, String userId, String gifterId, String gifterName) {
        TreeMap<String, Object> parameterMap = new TreeMap<>();
        parameterMap.put("broadcaster_id", broadcasterId);
        parameterMap.put("first", 1);
        parameterMap.put("gifter_id", gifterId);
        parameterMap.put("gifter_name", gifterName);
        parameterMap.put("user_id", userId);
        return parameterMap;
    }

    /**
     * @return The user ID of the broadcaster.
     */
    private final String broadcasterId;

    /**
     * @return The ID of the subscribed user.
     */
    private final Optional<String> userId;

    /**
     * @return ID of the user who gifted the sub.
     */
    private final Optional<String> gifterId;

    /**
     * @return Display name of the user who gifted the sub.
     */
    private final Optional<String> gifterName;

    /**
     * This webhook notifies you when:
     * <ul>
     *     <li>A payment has been processed for a subscription or unsubscription.
     *     <li>A user who is subscribed to a broadcaster notifies the broadcaster of their subscription in the chat.
     * </ul>
     *
     * @param broadcasterId Required. User ID of the broadcaster. Must match the User ID in the Bearer token.
     * @param userId        Optional. ID of the subscribed user. Currently only one user_id at a time can be queried.
     * @deprecated <a href="https://discuss.dev.twitch.tv/t/deprecation-of-websub-based-webhooks/32152">Will be decommissioned after 2021-09-16 in favor of EventSub</a>
     */
    @Deprecated
    public ChannelSubscriptionTopic(@NonNull String broadcasterId, String userId) {
        this(broadcasterId, userId, null, null);
    }

    /**
     * This webhook notifies you when:
     * <ul>
     *     <li>A payment has been processed for a subscription or unsubscription.
     *     <li>A user who is subscribed to a broadcaster notifies the broadcaster of their subscription in the chat.
     * </ul>
     *
     * @param broadcasterId Required. User ID of the broadcaster. Must match the User ID in the Bearer token.
     * @param userId        Optional. ID of the subscribed user.
     * @param gifterId      Optional. ID of the user who gifted the sub. Returns an empty string for non-gifts, "274598607" for anonymous gifts
     * @param gifterName    Optional. Display name of the user who gifted the sub. Returns an empty string for non-gifts, "AnAnonymousGifter" for anonymous gifts
     * @deprecated <a href="https://discuss.dev.twitch.tv/t/deprecation-of-websub-based-webhooks/32152">Will be decommissioned after 2021-09-16 in favor of EventSub</a>
     */
    @Deprecated
    public ChannelSubscriptionTopic(@NonNull String broadcasterId, String userId, String gifterId, String gifterName) {
        super(
            PATH,
            SubscriptionEventList.class,
            mapParameters(broadcasterId, userId, gifterId, gifterName)
        );
        this.broadcasterId = broadcasterId;
        this.userId = Optional.ofNullable(userId);
        this.gifterId = Optional.ofNullable(gifterId);
        this.gifterName = Optional.ofNullable(gifterName);
    }

}
