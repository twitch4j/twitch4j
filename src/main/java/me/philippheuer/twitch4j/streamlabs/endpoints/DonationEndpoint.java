package me.philippheuer.twitch4j.streamlabs.endpoints;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.auth.model.streamlabs.StreamlabsCredential;
import me.philippheuer.twitch4j.helper.HeaderRequestInterceptor;
import me.philippheuer.twitch4j.helper.QueryRequestInterceptor;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import me.philippheuer.twitch4j.streamlabs.model.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class DonationEndpoint extends AbstractStreamlabsEndpoint {

	/**
	 * Donation Endpoint
	 */
	public DonationEndpoint(StreamlabsClient streamlabsClient) {
		super(streamlabsClient);
	}

	/**
	 * Endpoint: Get Donations
	 *  Fetch donations for the authenticated user. Results are ordered by creation date, descending.
	 *  Limit: 100
	 * Requires Scope: donations.read
	 */
	public Optional<List<Donation>> getDonations(StreamlabsCredential credential) {
		// REST Request
		try {
			// Prepare
			String requestUrl = String.format("%s/donations", getStreamlabsClient().getEndpointUrl(), getStreamlabsClient().getStreamlabsEndpointVersion());
			RestTemplate restTemplate = getStreamlabsClient().getRestClient().getRestTemplate();

			// Query Parameters
			List<ClientHttpRequestInterceptor> localRestInterceptors = new ArrayList<ClientHttpRequestInterceptor>();
			localRestInterceptors.addAll(restTemplate.getInterceptors());
			localRestInterceptors.add(new QueryRequestInterceptor("access_token", credential.getOAuthToken()));
			localRestInterceptors.add(new QueryRequestInterceptor("currency", "EUR"));
			localRestInterceptors.add(new QueryRequestInterceptor("limit", "50"));
			restTemplate.setInterceptors(localRestInterceptors);

			// Request
			DonationList responseObject = restTemplate.getForObject(requestUrl, DonationList.class);

			return Optional.ofNullable(responseObject.getData());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}
}
