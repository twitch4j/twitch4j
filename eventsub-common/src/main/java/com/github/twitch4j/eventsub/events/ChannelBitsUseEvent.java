package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.enums.TwitchEnum;
import com.github.twitch4j.common.util.TwitchEnumDeserializer;
import com.github.twitch4j.eventsub.domain.BitsType;
import com.github.twitch4j.eventsub.domain.PowerUp;
import com.github.twitch4j.eventsub.domain.chat.Message;
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
public class ChannelBitsUseEvent extends EventSubUserChannelEvent {

    /**
     * The number of Bits used.
     */
    private int bits;

    /**
     * The type of Bits usage.
     */
    @NotNull
    @JsonDeserialize(using = TwitchEnumDeserializer.class)
    private TwitchEnum<BitsType> type;

    /**
     * Optional: An object that contains the user message and emote information needed to recreate the message.
     */
    @Nullable
    private Message message;

    /**
     * Optional: Data about Power-up.
     */
    @Nullable
    private PowerUp powerUp;

}
