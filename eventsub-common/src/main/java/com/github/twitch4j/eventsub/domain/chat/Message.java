package com.github.twitch4j.eventsub.domain.chat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class Message {

    /**
     * The chat message in plain text.
     */
    private String text;

    /**
     * Ordered list of chat message fragments.
     */
    private List<Fragment> fragments;

}
