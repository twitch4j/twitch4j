package me.philippheuer.util.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.BroadcasterType;
import me.philippheuer.twitch4j.enums.Scope;
import me.philippheuer.twitch4j.enums.StreamType;
import me.philippheuer.twitch4j.enums.SubscriptionPlan;
import me.philippheuer.twitch4j.enums.UserType;
import me.philippheuer.twitch4j.enums.VideoAccess;
import me.philippheuer.twitch4j.enums.VideoType;
import me.philippheuer.util.conversion.BroadcasterTypeDeserializer;
import me.philippheuer.util.conversion.DurationDeserializer;
import me.philippheuer.util.conversion.InstantClockDeserializer;
import me.philippheuer.util.conversion.ScopeDeserializer;
import me.philippheuer.util.conversion.StreamTypeDeserializer;
import me.philippheuer.util.conversion.SubscriptionPlanDeserializer;
import me.philippheuer.util.conversion.UnixTimestampDeserializer;
import me.philippheuer.util.conversion.UserTypeDeserializer;
import me.philippheuer.util.conversion.VideoAccessDeserializer;
import me.philippheuer.util.conversion.VideoTypeDeserializer;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

/**
 * Rest Client Wrapper
 *
 * @author Philipp Heuer
 * @version %I%, %G%
 * @since 1.0
 */

@Getter
@NoArgsConstructor
public class RestClient {

	/**
	 * REST Request Interceptors (adding header-values/query parameters/... to requests)
	 */
	private final List<ClientHttpRequestInterceptor> restInterceptors = new ArrayList<ClientHttpRequestInterceptor>();

	/**
	 * Adds a interceptor to the Rest Template.
	 *
	 * @param interceptor Interceptor, that will be executed prior to the rest request.
	 * @see HeaderRequestInterceptor
	 * @see QueryRequestInterceptor
	 */
	public void putRestInterceptor(ClientHttpRequestInterceptor interceptor) {
		restInterceptors.add(interceptor);
	}

	/**
	 * Gets a Rest Template.
	 *
	 * @return A RestTemplate for rest requests.
	 */
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = getPlainRestTemplate();

		putRestInterceptor(new LoggingRequestInterceptor());

		restTemplate.setInterceptors(restInterceptors);
		restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("https://api.twitch.tv/kraken"));

		return restTemplate;
	}

	/**
	 * Gets a Rest Template with authorization.
	 *
	 * @param OAuthCredential Credential, to add authentication headers to the rest template.
	 * @return A RestTemplate for rest requests.
	 */
	public RestTemplate getPrivilegedRestTemplate(OAuthCredential OAuthCredential) {
		// Get Rest Template
		RestTemplate restTemplate = getRestTemplate();

		// Request Interceptors (add Authorization)
		restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", OAuthCredential.getToken())));

		return restTemplate;
	}

	/**
	 * Gets a Rest Template without any interceptors.
	 *
	 * @return A RestTemplate for rest requests.
	 */
	public RestTemplate getPlainRestTemplate() {
		RestTemplate restTemplate = new RestTemplate(Collections.singletonList(new MappingJackson2HttpMessageConverter(getObjectMapper())));
		restTemplate.setErrorHandler(new RestErrorHandler());


		return restTemplate;
	}

	private ObjectMapper getObjectMapper() {
		SimpleModule simpleModule = new SimpleModule()
				.addDeserializer(Calendar.class, new UnixTimestampDeserializer())
				.addDeserializer(Scope.class, new ScopeDeserializer())
				.addDeserializer(BroadcasterType.class, new BroadcasterTypeDeserializer())
				.addDeserializer(Duration.class, new DurationDeserializer())
				.addDeserializer(Instant.class, new InstantClockDeserializer())
				.addDeserializer(Scope.class, new ScopeDeserializer())
				.addDeserializer(StreamType.class, new StreamTypeDeserializer())
				.addDeserializer(SubscriptionPlan.class, new SubscriptionPlanDeserializer())
				.addDeserializer(UserType.class, new UserTypeDeserializer())
				.addDeserializer(VideoAccess.class, new VideoAccessDeserializer())
				.addDeserializer(VideoType.class, new VideoTypeDeserializer());

		return new ObjectMapper()
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // Ignoring unknown properties
				.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
				.registerModule(simpleModule);
	}
}
