package twitch4j.chat;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
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
    private EventManager eventManager = new EventManager();

    /**
     * Credential Manager
     */
    @Wither
    private CredentialManager credentialManager = CredentialManagerBuilder.builder().build();

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
        TwitchChat twitchChat = new TwitchChat(this.eventManager, this.credentialManager);
        return twitchChat;
    }
}
