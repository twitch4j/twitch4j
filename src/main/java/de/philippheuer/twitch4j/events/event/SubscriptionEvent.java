package de.philippheuer.twitch4j.events.event;

import de.philippheuer.twitch4j.events.Event;
import de.philippheuer.twitch4j.model.*;

import lombok.*;

@Getter
@Setter
public class SubscriptionEvent extends Event {
	
	private final Subscription subscription;

	public SubscriptionEvent(Subscription subscription) {
		this.subscription = subscription;
	}
	
}
