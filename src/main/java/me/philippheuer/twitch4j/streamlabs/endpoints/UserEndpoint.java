package me.philippheuer.twitch4j.streamlabs.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.exceptions.CurrencyNotSupportedException;
import me.philippheuer.twitch4j.helper.QueryRequestInterceptor;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import me.philippheuer.twitch4j.streamlabs.model.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
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

			Logger.debug(this, "Sreamlabs: Fetched Donations for %s", getOAuthCredential().getDisplayName());

			return responseObject.getData();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Endpoint: Create Donation
	 * Create a donation for the authenticated user.
	 * Requires Scope: donations.create
	 *
	 * @param name The name of the donor
	 * @param identifier An identifier for this donor, which is used to group donations with the same donor. For example, if you create more than one donation with the same identifier, they will be grouped together as if they came from the same donor. Typically this is best suited as an email address, or a unique hash.
	 * @param currency The 3 letter currency code for this donation. Must be one of the supported currency codes. [https://twitchalerts.readme.io/v1.0/docs/currency-codes]
	 * @param amount The amount of this donation
	 * @param message Optional: The message from the donor
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

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("access_token", getOAuthCredential().getOAuthToken()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("name", name));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("identifier", identifier));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("amount", amount.toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("currency", currency.getCurrencyCode()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("message", message.orElse("")));

		// REST Request
		try {
			DonationCreate responseObject = restTemplate.getForObject(requestUrl, DonationCreate.class);

			Logger.debug(this, "Sreamlabs: Created new Donation for %s [%s %s - ID:%s]", getOAuthCredential().getDisplayName(), amount.toString(), currency.getCurrencyCode(), responseObject.getDonationId());

			return responseObject.getDonationId();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Endpoint: Create Alert
	 * Trigger a custom alert for the authenticated user.
	 * Requires Scope: alerts.create
	 *
	 * @param type This parameter determines which alert box this alert will show up in, and thus should be one of the following: follow, subscription, donation, or host
	 * @param message The message to show with this alert. If not supplied, no message will be shown. Surround special tokens with *s, for example: This is my *special* alert!
	 * @param duration How many seconds this alert should be displayed.
	 * @param special_text_color The color to use for special tokens. Must be a valid CSS color string.
	 * @param imageUrl The href pointing to an image resource to play when this alert shows. If an empty string is supplied, no image will be displayed.
	 * @param soundUrl The href pointing to a sound resource to play when this alert shows. If an empty string is supplied, no sound will be played.
	 * @return Success?
	 */
	public Boolean createAlert(String type, Optional<String> message, Optional<Integer> duration, Optional<String> special_text_color, Optional<String> imageUrl, Optional<String> soundUrl) {
		// Validate Parameters
		if (!Arrays.asList("follow", "subscription", "donation", "host").contains(type)) {
			throw new RuntimeException("Invalid Type");
		}

		// Endpoint
		String requestUrl = String.format("%s/alerts", getStreamlabsClient().getEndpointUrl());
		RestTemplate restTemplate = getStreamlabsClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("access_token", getOAuthCredential().getOAuthToken()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("type", type));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("message", message.orElse("")));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("duration", duration.orElse(10).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("special_text_color", special_text_color.orElse("")));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("image_href", imageUrl.orElse("")));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("sound_href", soundUrl.orElse("")));

		// REST Request
		try {
			AlertCreate responseObject = restTemplate.getForObject(requestUrl, AlertCreate.class);

			Logger.debug(this, "Sreamlabs: Created new Alert for %s", getOAuthCredential().getDisplayName());

			return responseObject.getSuccess();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
