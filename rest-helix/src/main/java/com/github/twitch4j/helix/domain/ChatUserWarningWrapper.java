package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ChatUserWarningWrapper {

    /**
     * Contains the single warning metadata object.
     */
    private List<ChatUserWarning> data;

    /**
     * @return a single object with the warning information.
     */
    public ChatUserWarning get() {
        return data.get(0);
    }

}
