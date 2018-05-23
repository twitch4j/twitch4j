package twitch4j.api.kraken.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelDoesNotExistException extends RuntimeException {

	private Long channelId;

	public ChannelDoesNotExistException(Long channelId, Throwable cause) {
		super("Couldn't retrieve channel [ID: " + channelId + "]", cause);
		setChannelId(channelId);
	}
}
