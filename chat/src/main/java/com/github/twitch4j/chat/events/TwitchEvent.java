package com.github.twitch4j.chat.events;

import com.github.philippheuer.events4j.core.domain.Event;
import com.github.twitch4j.chat.TwitchChat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class TwitchEvent extends Event {

	/**
	 * Constructor
	 */
	public TwitchEvent() {
		super();
	}

    @Setter(onMethod_ = { @ApiStatus.Internal })
    private TwitchChat twitchChat;

	/**
	 * Get TwitchChat
	 *
	 * @return TwitchChat Instance
	 */
	public TwitchChat getTwitchChat() {
		if (twitchChat != null) {
			return twitchChat;
		}

		return getServiceMediator().getService(TwitchChat.class, "twitch4j-chat");
	}
}
