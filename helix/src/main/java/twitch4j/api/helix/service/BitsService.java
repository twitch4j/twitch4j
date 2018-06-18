package twitch4j.api.helix.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twitch4j.api.helix.enums.Period;
import twitch4j.api.helix.json.BitsLeadeboardData;
import twitch4j.api.helix.json.BitsRank;
import twitch4j.api.helix.json.User;
import twitch4j.common.auth.ICredential;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.request.TwitchRequest;
import twitch4j.stream.rest.route.Route;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

public class BitsService extends AbstractService<BitsLeadeboardData> {

	private final ICredential credential;

	private volatile Integer count = 10;
	private volatile Period period = Period.ALL;
	@Nullable
	private volatile Instant startedAt;

	public BitsService(Router router, ICredential credential) {
		super(Route.get("/bits/leaderboard", BitsLeadeboardData.class), router);
		this.credential = credential;
	}

	public Optional<BitsRank> getByUser(User user) {
		return get(user).flatMap(data -> Mono.just(data.getData().get(0))).blockOptional();
	}

	public Flux<BitsRank> listAll() {
		return get().flatMapMany(data -> Flux.fromIterable(data.getData()));
	}

	private Mono<BitsLeadeboardData> get() {
		return get(null);
	}

	private Mono<BitsLeadeboardData> get(@Nullable User user) {
		TwitchRequest<BitsLeadeboardData> request = route.newRequest()
				.header("Authorization", "Bearer " + credential.accessToken())
				.query("period", period.toString().toLowerCase());

		if (!period.equals(Period.ALL) && startedAt != null) {
			request.query("started_at", formatRfc3339(startedAt));
		}

		if (user != null) {
			request.query("user_id", user.getId().toString());
		} else {
			request.query("count", count);
		}

		return request.exchange(router);
	}

	/**
	 * Formatting timestamp into RFC3339 in PST.
	 * @param instant timestamp
	 * @return Timestamp for the period over which the returned data is aggregated
	 */
	private String formatRfc3339(Instant instant) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
		dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("PST")));
		return dateFormat.format(Date.from(instant));
	}

	public BitsService count(Integer count) {
		if (count > 100) this.count = 100;
		else if (count < 1) this.count = 10;
		else this.count = count;
		return this;
	}

	public BitsService peroid(Period period) {
		this.period = period;
		return this;
	}

	public BitsService startedAt(Instant startedAt) {
		this.startedAt = startedAt;
		return this;
	}
}
