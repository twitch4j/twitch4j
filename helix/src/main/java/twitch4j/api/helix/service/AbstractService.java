package twitch4j.api.helix.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.route.Route;

/**
 * Abstract Service provides for each service in {@link twitch4j.api.helix.service Service Package}
 * @param <T> Response type for {@link twitch4j.stream.rest.route.Route}
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class AbstractService<T> {
	protected final Route<T> route;
	protected final Router router;
}
