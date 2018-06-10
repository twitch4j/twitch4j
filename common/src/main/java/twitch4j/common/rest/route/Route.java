package twitch4j.common.rest.route;

import io.netty.handler.codec.http.HttpMethod;
import java.util.Objects;
import twitch4j.common.rest.request.TwitchRequest;
import twitch4j.common.utils.RouteUtils;

/**
 * Provides a mapping between a Discord API endpoint and its response type.
 *
 * @param <T> the response type
 * @since 3.0
 */
public class Route<T> {

	private final HttpMethod method;
	private final String uriTemplate;
	private final Class<T> responseType;

	private Route(HttpMethod method, String uriTemplate, Class<T> responseType) {
		this.method = method;
		this.uriTemplate = uriTemplate;
		this.responseType = responseType;
	}

	public static <T> Route<T> get(String uri, Class<T> responseType) {
		return new Route<>(HttpMethod.GET, uri, responseType);
	}

	public static <T> Route<T> post(String uri, Class<T> responseType) {
		return new Route<>(HttpMethod.POST, uri, responseType);
	}

	public static <T> Route<T> put(String uri, Class<T> responseType) {
		return new Route<>(HttpMethod.PUT, uri, responseType);
	}

	public static <T> Route<T> patch(String uri, Class<T> responseType) {
		return new Route<>(HttpMethod.PATCH, uri, responseType);
	}

	public static <T> Route<T> delete(String uri, Class<T> responseType) {
		return new Route<>(HttpMethod.DELETE, uri, responseType);
	}

	public HttpMethod getMethod() {
		return method;
	}

	public Class<T> getResponseType() {
		return responseType;
	}

	/**
	 * Prepare a request, expanding this route template URI with the given parameters.
	 *
	 * @param uriVars the values to expand each template parameter
	 * @return a request that is ready to be routed
	 * @see TwitchRequest#exchange
	 */
	public TwitchRequest<T> newRequest(Object... uriVars) {
		return new TwitchRequest<>(this, RouteUtils.expand(getUriTemplate(), uriVars));
	}

	public String getUriTemplate() {
		return uriTemplate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(method, responseType, uriTemplate);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!obj.getClass().isAssignableFrom(Route.class)) {
			return false;
		}

		Route other = (Route) obj;

		return other.method.equals(method) && other.responseType.equals(responseType)
				&& other.uriTemplate.equals(uriTemplate);
	}
}
