package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class SentChatMessageWrapper {

    /**
     * Contains a single object with metadata about the sent message.
     */
    private List<SentChatMessage> data;

    /**
     * @return metadata about the sent message
     */
    public SentChatMessage get() {
        if (data == null || data.isEmpty()) return null; // should never happen
        return data.get(0);
    }

}
