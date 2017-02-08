package me.philippheuer.twitch4j.streamlabs.endpoints;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.auth.model.streamlabs.StreamlabsCredential;
import me.philippheuer.twitch4j.exceptions.CurrencyNotSupportedException;
import me.philippheuer.twitch4j.helper.HeaderRequestInterceptor;
import me.philippheuer.twitch4j.helper.QueryRequestInterceptor;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import me.philippheuer.twitch4j.streamlabs.model.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Currency;
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
	 * @param currency Donations are returned in target currency (absense: original currency) [List of valid currencies: https://twitchalerts.readme.io/v1.0/docs/currency-codes]
	 * @param limit Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
	 */
	public List<Donation> getDonations(StreamlabsCredential credential, Optional<Currency> currency, Optional<Integer> limit) {
		// Validate Parameters
		if(currency.isPresent()) {
			if(!getStreamlabsClient().getValidCurrencies().contains(currency.get().getCurrencyCode())) {
				throw new CurrencyNotSupportedException(currency.get());
			}
		}

		// Endpoint
		String requestUrl = String.format("%s/donations", getStreamlabsClient().getEndpointUrl());
		RestTemplate restTemplate = getStreamlabsClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("access_token", credential.getOAuthToken()));
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
