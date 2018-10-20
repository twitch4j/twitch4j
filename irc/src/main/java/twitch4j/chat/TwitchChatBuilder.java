package twitch4j.chat;

import com.github.philippheuer.events4j.EventManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;

/**
 * Twitch Chat
 *
 * Documentation: https://dev.twitch.tv/docs/irc
 */
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TwitchChatBuilder {

    /**
     * Event Manager
     */
    @Wither
    private EventManager eventManager;

    /**
     * BaseUrl
     */
    private String baseUrl = "https://api.twitch.tv/helix";

    /**
     * Initialize the builder
     * @return Twitch Chat Builder
     */
    public static TwitchChatBuilder builder() {
        return new TwitchChatBuilder();
    }

    /**
     * Twitch API Client (Helix)
     * @return TwitchHelix
     */
    public TwitchChat build() {
        return null;
    }
}
