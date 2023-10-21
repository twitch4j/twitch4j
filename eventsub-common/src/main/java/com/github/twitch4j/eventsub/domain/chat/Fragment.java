package com.github.twitch4j.eventsub.domain.chat;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class Fragment {

    /**
     * The type of message fragment.
     */
    @NotNull
    private Type type;

    /**
     * Optional: Metadata pertaining to the cheermote.
     */
    @Nullable
    private Cheermote cheermote;

    /**
     * Optional: Metadata pertaining to the emote.
     */
    @Nullable
    private Emote emote;

    /**
     * Optional: Metadata pertaining to the mention.
     */
    @Nullable
    private Mention mention;

    public enum Type {
        TEXT,
        CHEERMOTE,
        EMOTE,
        MENTION,
        @JsonEnumDefaultValue
        UNKNOWN
    }

}
