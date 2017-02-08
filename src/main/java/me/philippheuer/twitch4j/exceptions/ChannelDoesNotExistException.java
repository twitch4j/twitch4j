package me.philippheuer.twitch4j.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelDoesNotExistException extends RuntimeException {

	private Long channelId;

	public ChannelDoesNotExistException(Long channelId) {
		super();
		setChannelId(channelId);
	}
}
