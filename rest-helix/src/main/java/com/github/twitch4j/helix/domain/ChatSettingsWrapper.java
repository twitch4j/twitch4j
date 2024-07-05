package com.github.twitch4j.helix.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChatSettingsWrapper extends ValueWrapper<ChatSettings> {

    /**
     * @return a single object with all of the settings.
     */
    public ChatSettings getChatSettings() {
        return this.get();
    }

}
