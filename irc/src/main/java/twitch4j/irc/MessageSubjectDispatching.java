package twitch4j.irc;

import lombok.RequiredArgsConstructor;
import twitch4j.common.events.EventManager;
import twitch4j.common.events.EventSubscriber;

@RequiredArgsConstructor
public class MessageSubjectDispatching {
	private final EventManager eventManager;

	@EventSubscriber
	public void onServerMessage() {

	}
}
