package twitch4j.api.kraken.exceptions;

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
