package me.philippheuer.twitch4j.streamlabs.endpoints;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;

@Getter
@Setter
public class AbstractStreamlabsEndpoint {

	/**
	 * Holds the API Instance
	 */
	private StreamlabsClient streamlabsClient;

	/**
	 * AbstractTwitchEndpoint
	 *
	 * @param streamlabsClient Streamlabs Client
	 */
	public AbstractStreamlabsEndpoint(StreamlabsClient streamlabsClient) {
		// Properties
		setStreamlabsClient(streamlabsClient);
	}
}
