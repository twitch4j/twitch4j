package me.philippheuer.twitch4j.streamlabs.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.exceptions.CurrencyNotSupportedException;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.util.rest.LoggingRequestInterceptor;
import me.philippheuer.util.rest.QueryRequestInterceptor;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import me.philippheuer.twitch4j.streamlabs.model.Donation;
import me.philippheuer.twitch4j.streamlabs.model.DonationCreate;
import me.philippheuer.twitch4j.streamlabs.model.DonationList;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class DonationEndpoint extends AbstractStreamlabsEndpoint {

	/**
	 * Holds the credentials to the current user
	 */
	private OAuthCredential oAuthCredential;

	/**
	 * Stream Labs - Authenticated Endpoint
	 *
	 * @param streamlabsClient todo
	 * @param credential todo
	 */
	public DonationEndpoint(StreamlabsClient streamlabsClient, OAuthCredential credential) {
		super(streamlabsClient);
		setOAuthCredential(credential);
	}

	/**
	 * Endpoint: Get Donations
	 * Fetch donations for the authenticated user. Results are ordered by creation date, descending.
	 * Limit: 100
	 * Requires Scope: donations.read
	 *
	 * @param currency Donations are returned in target currency (absense: original currency) [List of valid currencies: https://twitchalerts.readme.io/v1.0/docs/currency-codes]
	 * @param limit    Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
	 * @return List of Donations
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
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("access_token", getOAuthCredential().getToken()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("currency", currency.isPresent() ? currency.get().getCurrencyCode() : "EUR"));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.orElse(50).toString()));

		// REST Request
		try {
			DonationList responseObject = restTemplate.getForObject(requestUrl, DonationList.class);

			return responseObject.getData();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Endpoint: Create Donation
	 * Create a donation for the authenticated user.
	 * Requires Scope: donations.create
	 *
	 * @param name       The name of the donor
	 * @param identifier An identifier for this donor, which is used to group donations with the same donor. For example, if you create more than one donation with the same identifier, they will be grouped together as if they came from the same donor. Typically this is best suited as an email address, or a unique hash.
	 * @param currency   The 3 letter currency code for this donation. Must be one of the supported currency codes. [https://twitchalerts.readme.io/v1.0/docs/currency-codes]
	 * @param amount     The amount of this donation
	 * @param message    Optional: The message from the donor
	 * @return ID (Long) of the created donation
	 */
	public Long createDonation(String name, String identifier, Currency currency, Double amount, Optional<String> message) {
		// Validate Parameters
		if (!getStreamlabsClient().getValidCurrencies().contains(currency.getCurrencyCode())) {
			throw new CurrencyNotSupportedException(currency);
		}

		// Endpoint
		String requestUrl = String.format("%s/donations", getStreamlabsClient().getEndpointUrl());
		RestTemplate restTemplate = getStreamlabsClient().getRestClient().getRestTemplate();

		// Post Data
		MultiValueMap<String, Object> postBody = new LinkedMultiValueMap<String, Object>();
		postBody.add("access_token", getOAuthCredential().getToken());
		postBody.add("name", name);
		postBody.add("identifier", identifier);
		postBody.add("amount", amount);
		postBody.add("currency", currency.getCurrencyCode());
		postBody.add("message", message.orElse(""));

		restTemplate.getInterceptors().add(new LoggingRequestInterceptor());

		// REST Request
		try {
			DonationCreate responseObject = restTemplate.postForObject(requestUrl, postBody, DonationCreate.class);

			Logger.debug(this, "Sreamlabs: Created new Donation for %s [%s %s - ID:%s]", getOAuthCredential().getDisplayName(), amount.toString(), currency.getCurrencyCode(), responseObject.getDonationId());

			return responseObject.getDonationId();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
