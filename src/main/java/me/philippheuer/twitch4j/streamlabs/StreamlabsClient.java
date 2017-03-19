package me.philippheuer.twitch4j.streamlabs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.streamlabs.endpoints.TokenEndpoint;
import me.philippheuer.util.rest.HeaderRequestInterceptor;
import me.philippheuer.util.rest.RestClient;
import me.philippheuer.twitch4j.streamlabs.endpoints.AlertEndpoint;
import me.philippheuer.twitch4j.streamlabs.endpoints.DonationEndpoint;
import me.philippheuer.twitch4j.streamlabs.endpoints.UserEndpoint;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StreamlabsClient {

	/**
	 * Streamlabs API Endpoint
	 */
	public final String streamlabsEndpoint = "https://streamlabs.com/api";
	/**
	 * Streamlabs API Version
	 */
	public final String streamlabsEndpointVersion = "v1.0";
	/**
	 * Rest Client
	 */
	private RestClient restClient = new RestClient();
	/**
	 * Streamlabs Client Id
	 */
	@Singular
	private String clientId;

	/**
	 * Streamlabs Client Secret
	 */
	@Singular
	private String clientSecret;

	/**
	 * Holds all valid currencies in streamlabs
	 */
	private List<String> validCurrencies = new ArrayList<String>() {{
		{
			add("AUD");
			add("BRL");
			add("CAD");
			add("CZK");
			add("DKK");
			add("EUR");
			add("HKD");
			add("ILS");
			add("MYR");
			add("MXN");
			add("NOK");
			add("NZD");
			add("PHP");
			add("PLN");
			add("GBP");
			add("RUB");
			add("SGD");
			add("SEK");
			add("CHF");
			add("THB");
			add("TRY");
			add("USD");
		}
	}};

	/**
	 * Class Constructor
	 *
	 * @param clientId     Streamlabs Application - Id
	 * @param clientSecret Streamlabs Application - Secret
	 */
	public StreamlabsClient(String clientId, String clientSecret) {
		super();

		setClientId(clientId);
		setClientSecret(clientSecret);

		// Initialize REST Client
		// - Valid User Agent, because Cloudflare is between us and the api.
		getRestClient().putRestInterceptor(new HeaderRequestInterceptor("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.85 Safari/537.36"));
	}

	/**
	 * Client Builder
	 *
	 * @param clientId     Streamlabs Application - Id
	 * @param clientSecret Streamlabs Application - Secret
	 * @return new instance of type StreamlabsClient
	 */
	@Builder(builderMethodName = "builder")
	public static StreamlabsClient streamlabsClientBuilder(String clientId, String clientSecret) {
		// Reqired Parameters
		Assert.notNull(clientId, "You need to provide a client id!");
		Assert.notNull(clientSecret, "You need to provide a client secret!");

		// Initalize instance
		final StreamlabsClient streamlabsClient = new StreamlabsClient(clientId, clientSecret);

		// Return builded instance
		return streamlabsClient;
	}

	/**
	 * Get the full api endpoint address.
	 *
	 * @return The full api endpoint url.
	 */
	public String getEndpointUrl() {
		return String.format("%s/%s", getStreamlabsEndpoint(), getStreamlabsEndpointVersion());
	}

	/**
	 * Get Token Endpoint
	 *
	 * @return user endpoint
	 */
	public TokenEndpoint getTokenEndpoint() {
		return new TokenEndpoint(this);
	}

	/**
	 * Get User Endpoint
	 *
	 * @param credential OAuthCredential
	 * @return user endpoint
	 */
	public UserEndpoint getUserEndpoint(OAuthCredential credential) {
		return new UserEndpoint(this, credential);
	}

	/**
	 * Get Donation Endpoint
	 *
	 * @param credential OAuthCredential
	 * @return donation endpoint
	 */
	public DonationEndpoint getDonationEndpoint(OAuthCredential credential) {
		return new DonationEndpoint(this, credential);
	}

	/**
	 * Get Alert Endpoint
	 *
	 * @param credential OAuthCredential
	 * @return alert endpoint
	 */
	public AlertEndpoint getAlertEndpoint(OAuthCredential credential) {
		return new AlertEndpoint(this, credential);
	}

}
