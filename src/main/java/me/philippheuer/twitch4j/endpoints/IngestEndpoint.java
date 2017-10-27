package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.model.Ingest;
import me.philippheuer.twitch4j.model.IngestList;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Getter
@Setter
public class IngestEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Get User by UserId
	 *
	 * @param client todo
	 */
	public IngestEndpoint(TwitchClient client) {
		super(client);
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
		// Endpoint
		String requestUrl = String.format("%s/ingests", Endpoints.API.getURL());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			IngestList responseObject = restTemplate.getForObject(requestUrl, IngestList.class);

			return responseObject.getIngests();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}
}
