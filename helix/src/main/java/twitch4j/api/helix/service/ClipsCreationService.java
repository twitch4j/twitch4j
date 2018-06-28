package twitch4j.api.helix.service;

import reactor.core.publisher.Mono;
import twitch4j.api.helix.model.ClipCreate;
import twitch4j.api.helix.model.ClipCreated;
import twitch4j.api.helix.model.User;
import twitch4j.common.auth.ICredential;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.request.TwitchRequest;
import twitch4j.stream.rest.route.Route;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Provides Clips Creator Service. <br>
 * <b>Required: </b> {@link twitch4j.common.auth.Scope#CLIPS_EDIT clips:edit}
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
public class ClipsCreationService extends AbstractService<ClipCreated> {
	private final ICredential credential;

	private volatile AtomicBoolean delay = new AtomicBoolean(false);

	public ClipsCreationService(Router router, ICredential credential) {
		super(Route.post("/clips", ClipCreated.class), router);
		this.credential = credential;
	}

	/**
	 * Getting clip from broadcaster ID
	 * @param broadcasterId Broadcaster ID aka User ID
	 * @return clip creation data with links to editing clip. You must be logged in as same authorized user.
	 */
	public Mono<ClipCreated> fromId(Long broadcasterId) {
		TwitchRequest<ClipCreated> request = route.newRequest()
				.header("Authorization", "Bearer " + credential.accessToken())
				.query("broadcaster_id" ,broadcasterId);

		if (delay.get()) {
			request.query("has_delay", delay.get());
		}

		return request.exchange(router);
	}

	/**
	 * Getting clip from broadcaster ID
	 * @param broadcaster Broadcaster aka {@link User}
	 * @return clip creation data with links to editing clip. You must be logged in as same authorized user.
	 */
	public Mono<ClipCreated> from(User broadcaster) {
		return fromId(broadcaster.getId());
	}

	public ClipsCreationService withDelay() {
		delay.set(true);
		return this;
	}

	public ClipsCreationService withoutDelay() {
		delay.set(false);
		return this;
	}
}
