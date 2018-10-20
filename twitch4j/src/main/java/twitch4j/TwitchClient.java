package twitch4j;

import com.github.philippheuer.events4j.EventManager;
import twitch4j.chat.TwitchChat;
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
     * Chat
     */
    private final TwitchChat chat;

    /**
     * Constructor
     */
    public TwitchClient(EventManager eventManager, TwitchHelix helix, TwitchChat chat) {
        this.eventManager = eventManager;
        this.helix = helix;
        this.chat = chat;
    }

    /**
     * Get Helix
     */
    public TwitchHelix getHelix() {
        if (this.helix == null) {
            throw new RuntimeException("You have not enabled the Helix Module! Please check out the documentation on Twitch4J -> Helix.");
        }

        return this.helix;
    }

    /**
     * Get Chat
     */
    public TwitchChat getChat() {
        if (this.chat == null) {
            throw new RuntimeException("You have not enabled the Chat Module! Please check out the documentation on Twitch4J -> Chat.");
        }

        return this.chat;
    }
}
