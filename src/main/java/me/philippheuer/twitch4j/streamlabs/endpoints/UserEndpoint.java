package me.philippheuer.twitch4j.streamlabs.endpoints;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.exceptions.CurrencyNotSupportedException;
import me.philippheuer.twitch4j.helper.QueryRequestInterceptor;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import me.philippheuer.twitch4j.streamlabs.model.Donation;
import me.philippheuer.twitch4j.streamlabs.model.DonationList;
import me.philippheuer.twitch4j.streamlabs.model.User;
import me.philippheuer.twitch4j.streamlabs.model.UserResponse;
import org.springframework.web.client.RestTemplate;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class UserEndpoint extends AbstractStreamlabsEndpoint {

	/**
	 * Holds the credentials to the current user
	 */
	private OAuthCredential oAuthCredential;

	/**
	 * Stream Labs - Authenticated Endpoint
	 */
	public UserEndpoint(StreamlabsClient streamlabsClient, OAuthCredential credential) {
		super(streamlabsClient);
		setOAuthCredential(credential);
	}

	/**
	 * Endpoint: Get the Streamlabs User
	 * Fetch information about the authenticated user.
	 * Requires Scope: none
	 */
	public Optional<User> getUser() {
		// Endpoint
		String requestUrl = String.format("%s/user", getStreamlabsClient().getEndpointUrl());
		RestTemplate restTemplate = getStreamlabsClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("access_token", getOAuthCredential().getOAuthToken()));

		// REST Request
		try {
			UserResponse responseObject = restTemplate.getForObject(requestUrl, UserResponse.class);

			return Optional.ofNullable(responseObject.getTwitch());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	/**
	 * Endpoint: Get Donations
	 * Fetch donations for the authenticated user. Results are ordered by creation date, descending.
	 * Limit: 100
	 * Requires Scope: donations.read
	 *
	 * @param currency Donations are returned in target currency (absense: original currency) [List of valid currencies: https://twitchalerts.readme.io/v1.0/docs/currency-codes]
	 * @param limit    Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
	 */
	public List<Donation> getDonations(Optional<Currency> currency, Optional<Integer> limit) {
		// Validate Parameters
		if (currency.isPresent()) {
			if (!getStreamlabsClient().getValidCurrencies().contains(currency.get().getCurrencyCode())) {
				throw new CurrencyNotSupportedException(currency.get());
			}
		}

		// Endpoint
		String requestUrl = String.format("%s/donations", getStreamlabsClient().getEndpointUrl());
		RestTemplate restTemplate = getStreamlabsClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("access_token", getOAuthCredential().getOAuthToken()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("currency", currency.isPresent() ? currency.get().getCurrencyCode() : "EUR"));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.orElse(50).toString()));

		// REST Request
		try {
			DonationList responseObject = restTemplate.getForObject(requestUrl, DonationList.class);

			return responseObject.getData();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
