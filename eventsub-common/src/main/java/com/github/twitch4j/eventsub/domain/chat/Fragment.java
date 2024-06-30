package com.github.twitch4j.eventsub.domain.chat;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class Fragment {

    /**
     * The type of message fragment.
     */
    @Getter(onMethod_ = { @NotNull })
    private Type type;

    /**
     * Message text in fragment.
     */
    @Getter(onMethod_ = { @NotNull })
    private String text;

    /**
     * Optional: Metadata pertaining to the cheermote.
     */
    @Nullable
    @JsonAlias("Cheermote")
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
