package twitch4j.api.helix.service;

import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twitch4j.api.helix.json.BitsLeadeboardData;
import twitch4j.api.helix.json.ClipCreate;
import twitch4j.api.helix.json.ClipCreated;
import twitch4j.common.auth.ICredential;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.request.TwitchRequest;
import twitch4j.stream.rest.route.Route;

import java.util.concurrent.atomic.AtomicBoolean;

public class ClipsCreationService extends AbstractService<ClipCreated> {
	private final ICredential credential;

	private volatile AtomicBoolean delay = new AtomicBoolean(false);

	public ClipsCreationService(Router router, ICredential credential) {
		super(Route.post("/clips", ClipCreated.class), router);
		this.credential = credential;
	}

	public Mono<ClipCreated> get(Long broadcasterId) {
		TwitchRequest<ClipCreated> request = route.newRequest()
				.header("Authorization", "Bearer " + credential.accessToken())
				.query("broadcaster_id" ,broadcasterId);

		if (delay.get()) {
			request.query("has_delay", delay.get());
		}

		return request.exchange(router);
	}

	public ClipsCreationService toggleDelay() {
		delay.set(!delay.get());
		return this;
	}
}
