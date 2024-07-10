package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.eventsub.domain.chat.Badge;
import com.github.twitch4j.eventsub.domain.chat.Cheer;
import com.github.twitch4j.eventsub.domain.chat.Message;
import com.github.twitch4j.eventsub.domain.chat.MessageType;
import com.github.twitch4j.eventsub.domain.chat.Reply;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelChatMessageEvent extends ChannelChatUserEvent {

    /**
     * A UUID that identifies the message.
     */
    private String messageId;

    /**
     * The structured chat message.
     */
    private Message message;

    /**
     * The color of the user’s name in the chat room.
     * This is a hexadecimal RGB color code with a "#" prefix.
     * This tag may be empty if it is never set.
     */
    private String color;

    /**
     * The chatting user's visible badges.
     */
    private List<Badge> badges;

    /**
     * The type of message.
     */
    @NotNull
    private MessageType messageType;

    /**
     * Metadata if this message is a cheer.
     */
    @Nullable
    private Cheer cheer;

    /**
     * Metadata if this message is a reply.
     */
    @Nullable
    private Reply reply;

    /**
     * The ID of a channel points custom reward that was redeemed.
     */
    @Nullable
    private String channelPointsCustomRewardId;

    /**
     * An ID for the type of animation selected as part of a "Message Effects" redemption.
     *
     * @see MessageType#POWER_UPS_MESSAGE_EFFECT
     */
    @Nullable
    private String channelPointsAnimationId;

}
