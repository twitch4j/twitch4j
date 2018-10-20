package twitch4j;

import com.github.philippheuer.events4j.EventManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import twitch4j.helix.TwitchHelix;
import twitch4j.helix.TwitchHelixBuilder;

/**
 * Builder to get a TwitchClient Instance by provided various options, to provide the user with a lot of customizable options.
 */
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TwitchClientBuilder {

    /**
     * Client ID
     */
    @Wither
    private String clientId;

    /**
     * Client Secret
     */
    @Wither
    private String clientSecret;

    /**
     * EventManager
     */
    @Wither
    private EventManager eventManager = new EventManager();

    /**
     * Initialize the builder
     *
     * @return Twitch Client Builder
     */
    public static TwitchClientBuilder builder() {
        return new TwitchClientBuilder();
    }

    /**
     * Initialize
     *
     * @return {@link TwitchClient} initialized class
     */
    public TwitchClient build() {
        // Client Id / Client Secret
        if (this.clientId == null || this.clientSecret == null) {
            log.warn("You have not provided the required Client-Id/Client-Secret to use the rest api, defaulting to the twitch official id. You may not be able to use some features, please check out the docs on how to use Twitch4J!");
            this.clientId = "jzkbprff40iqj646a697cyrvl0zt2m6";
            this.clientSecret = "**secret**";
        }

        // Module: Helix
        TwitchHelix helix = TwitchHelixBuilder.builder()
            .withClientId(this.clientId)
            .withClientSecret(this.clientSecret)
            .withEventManager(eventManager)
            .build();

        // Module: Client
        final TwitchClient client = new TwitchClient(eventManager, helix);

        // Return new Client Instance
        return client;
    }

}
