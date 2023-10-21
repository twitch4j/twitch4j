package com.github.twitch4j.eventsub.domain.chat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Cheermote {

    /**
     * The name portion of the Cheermote string that you use in chat to cheer Bits.
     * The full Cheermote string is the concatenation of {prefix} + {number of Bits}.
     * For example, if the prefix is “Cheer” and you want to cheer 100 Bits,
     * the full Cheermote string is Cheer100.
     * When the Cheermote string is entered in chat, Twitch converts it to
     * the image associated with the Bits tier that was cheered.
     */
    private String prefix;

    /**
     * The amount of bits cheered.
     */
    private Integer bits;

    /**
     * The tier level of the cheermote.
     */
    private Integer tier;

}
