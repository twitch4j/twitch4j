package twitch4j.chat.events;

import com.github.philippheuer.events4j.domain.Event;
import lombok.Data;
import twitch4j.chat.TwitchChat;

@Data
public abstract class TwitchEvent extends Event {

	/**
	 * Constructor
	 */
	public TwitchEvent() {
		super();
	}

	/**
     * Get TwitchChat
	 */
	public TwitchChat getTwitchChat() {
	    return getServiceMediator().getService(TwitchChat.class, "twitch4j-chat");
    }
}
