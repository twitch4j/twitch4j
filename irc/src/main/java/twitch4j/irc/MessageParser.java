package twitch4j.irc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import twitch4j.common.events.EventManager;

@Getter
@RequiredArgsConstructor
public class MessageParser {
	private final EventManager eventManager;
}
