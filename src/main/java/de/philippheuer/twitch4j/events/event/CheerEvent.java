package de.philippheuer.twitch4j.events.event;

import de.philippheuer.twitch4j.events.Event;
import de.philippheuer.twitch4j.model.*;
import lombok.*;

@Getter
@Setter
public class CheerEvent extends Event {
	
	private final Cheer cheer;

	public CheerEvent(Cheer cheer) {
		this.cheer = cheer;
	}
	
}
