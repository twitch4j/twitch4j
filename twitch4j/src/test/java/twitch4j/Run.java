package twitch4j;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.events4j.EventManager;

public class Run {

    public static void main(String[] args) {
        // external event manager (for shared module usage)
        EventManager eventManager = new EventManager();

        // external credential manager ( for shared module usage)
        CredentialManager credentialManager = CredentialManagerBuilder.builder().build();

        // construct twitchClient
        TwitchClient twitchClient = TwitchClientBuilder.builder()
            .withEventManager(eventManager)
            .withCredentialManager(credentialManager)
            .withEnableHelix(true)
            .withEnableChat(true)
            .build();

    }

}
