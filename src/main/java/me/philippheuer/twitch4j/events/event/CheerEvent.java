package me.philippheuer.twitch4j.events.event;

import lombok.*;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.*;

@Getter
@Setter
public class CheerEvent extends Event {

	private final Cheer cheer;

	public CheerEvent(Cheer cheer) {
		this.cheer = cheer;
	}

}
