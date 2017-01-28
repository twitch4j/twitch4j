package me.philippheuer.twitch4j.streamlabs.endpoints;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import me.philippheuer.twitch4j.streamlabs.model.*;

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
			String requestUrl = String.format("%s/donations", getStreamlabsClient().getEndpointUrl(), getStreamlabsClient().getStreamlabsEndpointVersion());
			DonationList responseObject = getStreamlabsClient().getRestClient().getRestTemplate().getForObject(requestUrl, DonationList.class);

			return Optional.ofNullable(responseObject.getData());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}
}
