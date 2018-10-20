package twitch4j;

import com.github.philippheuer.events4j.EventManager;
import twitch4j.helix.TwitchHelix;

public class TwitchClient {

    /**
     * Event Manager
     */
    private final EventManager eventManager;

    /**
     * API: Helix
     */
    private final TwitchHelix helix;

    /**
     * Constructor
     */
    public TwitchClient(EventManager eventManager, TwitchHelix helix) {
        this.eventManager = eventManager;
        this.helix = helix;
    }

    /**
     * Helix Getter
     */
    public TwitchHelix getHelix() {
        if (this.helix == null) {
            throw new RuntimeException("You have not enabled the Helix Module! Please check out the documentation on Twitch4J -> Helix.");
        }

        return this.helix;
    }
}
