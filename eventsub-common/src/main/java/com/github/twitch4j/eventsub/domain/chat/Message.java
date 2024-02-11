package com.github.twitch4j.eventsub.domain.chat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class Message {

    private static final String ACTION_PREFIX = "\u0001ACTION ";

    /**
     * The chat message in plain text.
     */
    private String text;

    /**
     * Ordered list of chat message fragments.
     */
    private List<Fragment> fragments;

    /**
     * @return whether the user sent an action (i.e., "/me ") message
     */
    public boolean isAction() {
        return text.startsWith(ACTION_PREFIX) && text.endsWith("\u0001");
    }

    /**
     * @return the full message text without any ACTION prefix/suffix
     */
    public String getCleanedText() {
        return isAction() ? text.substring(ACTION_PREFIX.length(), text.length() - 1) : text;
    }

}
