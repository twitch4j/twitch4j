package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.util.TwitchUtils;
import com.github.twitch4j.eventsub.domain.chat.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelChatNotificationEvent extends ChannelChatUserEvent {

    /**
     * Whether the chatter is anonymous.
     */
    @Accessors(fluent = true)
    @JsonProperty("chatter_is_anonymous")
    private Boolean isChatterAnonymous;

    /**
     * The color of the user's name in the chat room.
     */
    private String color;

    /**
     * The user's visible badges in the chat room.
     */
    private List<Badge> badges;

    /**
     * The message Twitch shows in the chat room for this notice.
     */
    private String systemMessage;

    /**
     * A UUID that identifies the message.
     */
    private String messageId;

    /**
     * The structured chat message.
     */
    private Message message;

    /**
     * The type of notice.
     */
    private NoticeType noticeType;

    /**
     * The broadcaster user ID of the channel the message was sent from.
     * <p>
     * Is null when the message happens in the same channel as the broadcaster.
     * Is not null when in a shared chat session, and the action happens in the channel of a participant other than the broadcaster.
     */
    @Nullable
    private String sourceBroadcasterUserId;

    /**
     * The user name of the broadcaster of the channel the message was sent from.
     * <p>
     * Is null when the message happens in the same channel as the broadcaster.
     * Is not null when in a shared chat session, and the action happens in the channel of a participant other than the broadcaster.
     */
    @Nullable
    private String sourceBroadcasterUserName;

    /**
     * The login of the broadcaster of the channel the message was sent from.
     * <p>
     * Is null when the message happens in the same channel as the broadcaster.
     * Is not null when in a shared chat session, and the action happens in the channel of a participant other than the broadcaster.
     */
    @Nullable
    private String sourceBroadcasterUserLogin;

    /**
     * The UUID that identifies the source message from the channel the message was sent from.
     * <p>
     * Is null when the message happens in the same channel as the broadcaster.
     * Is not null when in a shared chat session, and the action happens in the channel of a participant other than the broadcaster.
     */
    @Nullable
    private String sourceMessageId;

    /**
     * The list of chat badges for the chatter in the channel the message was sent from.
     * <p>
     * Is null when the message happens in the same channel as the broadcaster.
     * Is not null when in a shared chat session, and the action happens in the channel of a participant other than the broadcaster.
     */
    @Nullable
    private List<Badge> sourceBadges;

    /**
     * Information about the sub event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#SUB}.
     */
    @Nullable
    private Subscription sub;

    /**
     * Information about the sub event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#SHARED_CHAT_SUB}.
     * This field is for a notice that happened for a channel in a shared chat session
     * other than the broadcaster in the subscription condition.
     */
    @Nullable
    private Subscription sharedChatSub;

    /**
     * Information about the resub event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#RESUB}.
     */
    @Nullable
    private Resubscription resub;

    /**
     * Information about the resub event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#SHARED_CHAT_RESUB}.
     * This field is for a notice that happened for a channel in a shared chat session
     * other than the broadcaster in the subscription condition.
     */
    @Nullable
    private Resubscription sharedChatResub;

    /**
     * Information about the gift sub event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#SUB_GIFT}.
     */
    @Nullable
    private SubGift subGift;

    /**
     * Information about the gift sub event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#SHARED_CHAT_SUB_GIFT}.
     * This field is for a notice that happened for a channel in a shared chat session
     * other than the broadcaster in the subscription condition.
     */
    @Nullable
    private SubGift sharedChatSubGift;

    /**
     * Information about the community gift sub event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#COMMUNITY_SUB_GIFT}.
     */
    @Nullable
    private CommunitySubGift communitySubGift;

    /**
     * Information about the community gift sub event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#SHARED_CHAT_COMMUNITY_SUB_GIFT}.
     * This field is for a notice that happened for a channel in a shared chat session
     * other than the broadcaster in the subscription condition.
     */
    @Nullable
    private CommunitySubGift sharedChatCommunitySubGift;

    /**
     * Information about the community gift paid upgrade event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#GIFT_PAID_UPGRADE}.
     */
    @Nullable
    private GiftPaidUpgrade giftPaidUpgrade;

    /**
     * Information about the community gift paid upgrade event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#SHARED_CHAT_GIFT_PAID_UPGRADE}.
     * This field is for a notice that happened for a channel in a shared chat session
     * other than the broadcaster in the subscription condition.
     */
    @Nullable
    private GiftPaidUpgrade sharedChatGiftPaidUpgrade;

    /**
     * Information about the Prime gift paid upgrade event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#PRIME_PAID_UPGRADE}.
     */
    @Nullable
    private PrimePaidUpgrade primePaidUpgrade;

    /**
     * Information about the Prime gift paid upgrade event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#SHARED_CHAT_PRIME_PAID_UPGRADE}.
     * This field is for a notice that happened for a channel in a shared chat session
     * other than the broadcaster in the subscription condition.
     */
    @Nullable
    private PrimePaidUpgrade sharedChatPrimePaidUpgrade;

    /**
     * Information about the raid event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#RAID}.
     */
    @Nullable
    private Raid raid;

    /**
     * Information about the raid event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#SHARED_CHAT_RAID}.
     * This field is for a notice that happened for a channel in a shared chat session
     * other than the broadcaster in the subscription condition.
     */
    @Nullable
    private Raid sharedChatRaid;

    /**
     * Information about the unraid event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#UNRAID}.
     */
    @Nullable
    private Unraid unraid;

    /**
     * Information about the pay it forward event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#PAY_IT_FORWARD}.
     */
    @Nullable
    private GiftPayForward payItForward;

    /**
     * Information about the pay it forward event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#SHARED_CHAT_PAY_IT_FORWARD}.
     * This field is for a notice that happened for a channel in a shared chat session
     * other than the broadcaster in the subscription condition.
     */
    @Nullable
    private GiftPayForward sharedChatPayItForward;

    /**
     * Information about the announcement event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#ANNOUNCEMENT}.
     */
    @Nullable
    private Announcement announcement;

    /**
     * Information about the announcement event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#SHARED_CHAT_ANNOUNCEMENT}.
     * This field is for a notice that happened for a channel in a shared chat session
     * other than the broadcaster in the subscription condition.
     */
    @Nullable
    private Announcement sharedChatAnnouncement;

    /**
     * Information about the charity donation event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#CHARITY_DONATION}.
     */
    @Nullable
    private CharityDonation charityDonation;

    /**
     * Information about the bits badge tier event.
     * Null if {@link #getNoticeType()} is not {@link NoticeType#BITS_BADGE_TIER}.
     */
    @Nullable
    private BitsBadge bitsBadgeTier;

    /**
     * @return {@link #getBadges()} as {@link CommandPermission}, if {@link #getBadges()} is not null.
     */
    @Nullable
    public Set<CommandPermission> getPermissions() {
        return badges != null ? TwitchUtils.getPermissions(badges, Badge::getSetId, Badge::getId) : null;
    }

}
