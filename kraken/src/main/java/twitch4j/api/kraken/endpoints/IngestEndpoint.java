package twitch4j.api.kraken.endpoints;

import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;
import twitch4j.api.kraken.json.Ingest;
import twitch4j.api.kraken.json.IngestList;

@Slf4j
public class IngestEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Get User by UserId
	 *
	 * @param restTemplate {@link RestTemplate}
	 */
	public IngestEndpoint(RestTemplate restTemplate) {
		super(restTemplate);
	}

	/**
	 * Endpoint: Get Ingest Server List
	 * Gets a list of Twitch ingest servers.
	 * The Twitch ingesting system is the first stop for a broadcast stream. An ingest server receives your stream, and the
	 * ingesting system authorizes and registers streams, then prepares them for viewers.
	 * Requires Scope: none
	 *
	 * @return todo
	 */
	public List<Ingest> getIngestServer() {
		// REST Request
		try {
			return restTemplate.getForObject("/ingests", IngestList.class).getIngests();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Collections.emptyList();
		}
	}
}
