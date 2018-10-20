package twitch4j;

import com.github.philippheuer.events4j.EventManager;
import lombok.AccessLevel;
import lombok.Getter;
import twitch4j.helix.TwitchHelix;


@Getter(AccessLevel.PUBLIC)
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
}
