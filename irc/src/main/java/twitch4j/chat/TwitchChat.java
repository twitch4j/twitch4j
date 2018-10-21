package twitch4j.chat;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.events4j.EventManager;

public class TwitchChat {

    /**
     * EventManager
     */
    private final EventManager eventManager;

    /**
     * CredentialManager
     */
    private final CredentialManager credentialManager;

    /**
     * Constructor
     *
     * @param eventManager EventManager
     */
    public TwitchChat(EventManager eventManager, CredentialManager credentialManager) {
        this.eventManager = eventManager;
        this.credentialManager = credentialManager;

        // register with serviceMediator
        this.eventManager.getServiceMediator().addService("twitch4j-chat", this);
    }

}
