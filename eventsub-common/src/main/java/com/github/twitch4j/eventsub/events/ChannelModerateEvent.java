package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.eventsub.domain.moderation.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelModerateEvent extends EventSubModeratorEvent {

    /**
     * The channel ID in which the action originally occurred.
     * Is null when the moderator action happens in the same channel as the broadcaster.
     * Is not null when in a shared chat session, and the action happens in the channel of a participant other than the broadcaster.
     */
    @Nullable
    private String sourceBroadcasterUserId;

    /**
     * The channel login name in which the action originally occurred.
     * Is null when the moderator action happens in the same channel as the broadcaster.
     * Is not null when in a shared chat session, and the action happens in the channel of a participant other than the broadcaster.
     */
    @Nullable
    private String sourceBroadcasterUserLogin;

    /**
     * The channel display name in which the action originally occurred.
     * Is null when the moderator action happens in the same channel as the broadcaster.
     * Is not null when in a shared chat session, and the action happens in the channel of a participant other than the broadcaster.
     */
    @Nullable
    private String sourceBroadcasterUserName;

    /**
     * The action performed.
     * <p>
     * Note that the following actions do not have any associated metadata:
     * {@link Action#CLEAR}, {@link Action#EMOTEONLY}, {@link Action#EMOTEONLYOFF}, {@link Action#FOLLOWERSOFF},
     * {@link Action#SLOWOFF}, {@link Action#SUBSCRIBERS}, {@link Action#SUBSCRIBERSOFF},
     * {@link Action#UNIQUECHAT}, and {@link Action#UNIQUECHATOFF}.
     */
    private @NotNull Action action;

    /**
     * Metadata associated with the followers command.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#FOLLOWERS}.
     */
    private @Nullable FollowersMode followers;

    /**
     * Metadata associated with the slow command.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#SLOW}.
     */
    private @Nullable SlowMode slow;

    /**
     * Metadata associated with the vip command.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#VIP}.
     */
    private @Nullable UserTarget vip;

    /**
     * Metadata associated with the unvip command.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#UNVIP}.
     */
    private @Nullable UserTarget unvip;

    /**
     * Metadata associated with the mod command.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#MOD}.
     */
    private @Nullable UserTarget mod;

    /**
     * Metadata associated with the unmod command.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#UNMOD}.
     */
    private @Nullable UserTarget unmod;

    /**
     * Metadata associated with the ban command.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#BAN}.
     */
    private @Nullable BanTarget ban;

    /**
     * Metadata associated with the ban command.
     * This field is for an action that happened for a channel in a shared chat session
     * other than the broadcaster in the subscription condition.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#SHARED_CHAT_BAN}.
     */
    private @Nullable BanTarget sharedChatBan;

    /**
     * Metadata associated with the unban command.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#UNBAN}.
     */
    private @Nullable UserTarget unban;

    /**
     * Metadata associated with the unban command.
     * This field is for an action that happened for a channel in a shared chat session
     * other than the broadcaster in the subscription condition.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#SHARED_CHAT_UNBAN}.
     */
    private @Nullable UserTarget sharedChatUnban;

    /**
     * Metadata associated with the timeout command.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#TIMEOUT}.
     */
    private @Nullable TimeoutTarget timeout;

    /**
     * Metadata associated with the timeout command.
     * This field is for an action that happened for a channel in a shared chat session
     * other than the broadcaster in the subscription condition.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#SHARED_CHAT_TIMEOUT}.
     */
    private @Nullable TimeoutTarget sharedChatTimeout;

    /**
     * Metadata associated with the untimeout command.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#UNTIMEOUT}.
     */
    private @Nullable UserTarget untimeout;

    /**
     * Metadata associated with the untimeout command.
     * This field is for an action that happened for a channel in a shared chat session
     * other than the broadcaster in the subscription condition.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#SHARED_CHAT_UNTIMEOUT}.
     */
    private @Nullable UserTarget sharedChatUntimeout;

    /**
     * Metadata associated with the raid command.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#RAID}.
     */
    private @Nullable RaidTarget raid;

    /**
     * Metadata associated with the unraid command.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#UNRAID}.
     */
    private @Nullable UserTarget unraid;

    /**
     * Metadata associated with the delete command.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#DELETE}.
     */
    private @Nullable DeleteTarget delete;

    /**
     * Metadata associated with the delete command.
     * This field is for an action that happened for a channel in a shared chat session
     * other than the broadcaster in the subscription condition.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#SHARED_CHAT_DELETE}.
     */
    private @Nullable DeleteTarget sharedChatDelete;

    /**
     * Metadata associated with automod terms changes.
     * <p>
     * This is only populated when {@link #getAction()} is
     * {@link Action#ADD_BLOCKED_TERM} or {@link Action#ADD_PERMITTED_TERM} or
     * {@link Action#DELETE_BLOCKED_TERM} or {@link Action#DELETE_PERMITTED_TERM}.
     */
    private @Nullable AutomodTerms automodTerms;

    /**
     * Metadata associated with actioning an unban request.
     * <p>
     * This is only populated when {@link #getAction()} is
     * {@link Action#APPROVE_UNBAN_REQUEST} or {@link Action#DENY_UNBAN_REQUEST}.
     */
    private @Nullable UnbanRequest unbanRequest;

    /**
     * Metadata associated with the warn command.
     * <p>
     * This is only populated when {@link #getAction()} is {@link Action#WARN}.
     */
    private @Nullable Warning warn;

}
