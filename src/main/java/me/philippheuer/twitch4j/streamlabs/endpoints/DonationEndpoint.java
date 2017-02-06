package me.philippheuer.twitch4j.streamlabs.endpoints;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.helper.HeaderRequestInterceptor;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import me.philippheuer.twitch4j.streamlabs.model.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
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
	 * Requires Scope: donations.read
	 */
	public Optional<List<Donation>> getDonations() {
		// REST Request
		try {
			// Prepare
			String requestUrl = String.format("%s/donations", getStreamlabsClient().getEndpointUrl(), getStreamlabsClient().getStreamlabsEndpointVersion());
			RestTemplate restTemplate = getStreamlabsClient().getRestClient().getRestTemplate();

			// Query Parameters
			List<ClientHttpRequestInterceptor> localRestInterceptors = new ArrayList<ClientHttpRequestInterceptor>();
			localRestInterceptors.addAll(getStreamlabsClient().getRestClient().getRestInterceptors());
			// TODO: Get access token from credential manager
			localRestInterceptors.add(new HeaderRequestInterceptor("access_token", "value"));
			localRestInterceptors.add(new HeaderRequestInterceptor("currency", "EUR"));
			localRestInterceptors.add(new HeaderRequestInterceptor("limit", "50"));
			restTemplate.setInterceptors(localRestInterceptors);

			// Request
			DonationList responseObject = restTemplate.getForObject(requestUrl, DonationList.class);

			return Optional.ofNullable(responseObject.getData());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}
}
