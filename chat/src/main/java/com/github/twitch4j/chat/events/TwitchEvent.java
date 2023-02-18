package com.github.twitch4j.chat.events;

import com.github.philippheuer.events4j.core.domain.Event;
import com.github.twitch4j.chat.TwitchChat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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

    @Getter
    @Setter(onMethod_ = { @ApiStatus.Internal })
    private TwitchChat twitchChat;
}
