package twitch4j.api;

import lombok.RequiredArgsConstructor;
import twitch4j.Configuration;
import twitch4j.api.helix.exceptions.ScopeIsMissingException;
import twitch4j.api.helix.service.*;
import twitch4j.common.auth.ICredential;
import twitch4j.common.auth.Scope;
import twitch4j.stream.rest.request.Router;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


/**
 * The <b>NEW</b> Twitch API (Helix) provides tools for developing integrations with Twitch.
 * Using {@link twitch4j.stream.rest.request.Router Router} to provides reactive non=blocking response
 * components using <a href="https://projectreactor.io/">Project Reactor</a>.
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
@RequiredArgsConstructor
public class TwitchHelix {
	/**
	 * Http Router
	 */
	private final Router router;

	/**
	 * Initialize the TwitchHelix Endpoints
	 */
	public TwitchHelix(Configuration configuration) {
		Map<String, String> headers = new LinkedHashMap<>();

		// set user agent
		if (!configuration.getUserAgent().isEmpty()) {
			headers.put("User-Agent", configuration.getUserAgent());
		}

		// set client id
		headers.put("Client-ID", configuration.getClientId());

		// create router instance
		router = Configuration.buildRouter("https://api.twitch.tv/helix", headers);
	}

	/**
	 * The Bits Service
	 *
	 * Required Permissions: {@link twitch4j.common.auth.Scope#BITS_READ}
	 * @return A new instance of the BitsService
	 */
	public BitsService getBitsLeaderboad(ICredential credential) {
		if (!credential.scopes().contains(Scope.BITS_READ)) {
			throw new ScopeIsMissingException(Scope.BITS_READ);
		}
		return new BitsService(router, credential);
	}

	/**
	 * The Clips Service
	 *
	 * @return A new instance of the ClipsService
	 */
	public ClipsService getClipsService() {
		return new ClipsService(router);
	}

	/**
	 * The ClipsCreation Service
	 *
	 * Required Permissions: {@link twitch4j.common.auth.Scope#CLIPS_EDIT}
	 * @return A new instance of the ClipsCreationService
	 */
	public ClipsCreationService createClip(ICredential credential) {
		if (!credential.scopes().contains(Scope.CLIPS_EDIT)) {
			throw new ScopeIsMissingException(Scope.CLIPS_EDIT);
		}
		return new ClipsCreationService(router, credential);
	}

	/**
	 * The Follows Service
	 *
	 * @return A new instance of the FollowsService
	 */
	public FollowsService getFollowsService() {
		return new FollowsService(router);
	}

	/**
	 * The Games Service
	 *
	 * @return A new instance of the GamesService
	 */
	public GamesService getGamesService() {
		return new GamesService(router);
	}

	/**
	 * The Streams Service
	 *
	 * @return A new instance of the StreamsService
	 */
	public StreamsService getStreamsService() {
		return new StreamsService(router);
	}

	/**
	 * The Users Service
	 *
	 * @return A new instance of the UsersService
	 */
	public UsersService getUsersService() {
		return getUsersService(null);
	}

	/**
	 * The Users Service
	 *
	 * Required Permissions: {@link twitch4j.common.auth.Scope#USER_READ}
	 * @return A new instance of the UsersService
	 */
	public UsersService getUsersService(@Nullable ICredential credential) {
		if (credential != null && !credential.scopes().contains(Scope.USER_READ)) {
			throw new ScopeIsMissingException(Scope.USER_READ);
		}
		return new UsersService(router, Optional.ofNullable(credential));
	}

	/**
	 * The Video Service
	 *
	 * @return A new instance of the VideoService
	 */
	public VideoService getVideoService() {
		return new VideoService(router);
	}
}
