package me.philippheuer.twitch4j.events.event;

import lombok.*;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.*;

@Getter
@Setter
public class SubscriptionEvent extends Event {

	private final Subscription subscription;

	public SubscriptionEvent(Subscription subscription) {
		this.subscription = subscription;
	}

}
