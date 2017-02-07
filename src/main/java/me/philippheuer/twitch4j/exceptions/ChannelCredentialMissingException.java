package me.philippheuer.twitch4j.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelCredentialMissingException extends RuntimeException {

	private Long channelId;

	public ChannelCredentialMissingException(Long channelId) {
		super();
		setChannelId(channelId);
	}
}
