package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class ChatSettingsWrapper {

    /**
     * The list of chat settings. The list contains a single object with all the settings.
     */
    @Getter(AccessLevel.PRIVATE)
    private List<ChatSettings> data;

    /**
     * @return a single object with all of the settings.
     */
    public ChatSettings getChatSettings() {
        return data == null || data.isEmpty() ? null : data.get(0);
    }

}
