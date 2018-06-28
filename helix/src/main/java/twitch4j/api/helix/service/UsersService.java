package twitch4j.api.helix.service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twitch4j.api.helix.exceptions.ScopeIsMissingException;
import twitch4j.api.helix.model.User;
import twitch4j.api.helix.model.UserList;
import twitch4j.common.auth.ICredential;
import twitch4j.common.auth.Scope;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.request.TwitchRequest;
import twitch4j.stream.rest.route.Route;

import javax.naming.SizeLimitExceededException;
import javax.security.auth.login.CredentialNotFoundException;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Provides User Service. <br>
 * <b>Optional: </b> {@link twitch4j.common.auth.Scope#USER_EDIT user:edit}
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
@Slf4j
public class UsersService extends AbstractService<UserList> {

	private final Optional<ICredential> credential;

	private final Set<Long> userIds = new LinkedHashSet<>();
	private final Set<String> userLogins = new LinkedHashSet<>();

	public UsersService(Router router, Optional<ICredential> credential) {
		super(Route.get("/users", UserList.class), router);
		this.credential = credential;
	}

	/**
	 * Adding user Id
	 * @param userId user ID
	 * @return
	 */
	public UsersService userId(Long userId) {
		if (userIds.size() == 100) {
			SizeLimitExceededException ex = new SizeLimitExceededException("Limit of user_id's is exceeded. Max 100.");
			log.error(ex.getMessage(), ex);
		} else  {
			this.userIds.add(userId);
		}
		return this;
	}

	/**
	 * Adding username/login
	 * @param username username or login (not display name)
	 * @return
	 */
	public UsersService username(String username) {
		if (userLogins.size() == 100) {
			SizeLimitExceededException ex = new SizeLimitExceededException("Limit of login's is exceeded. Max 100.");
			log.error(ex.getMessage(), ex);
		} else  {
			this.userLogins.add(username);
		}
		return this;
	}

	/**
	 * Fetching all users
	 * @return
	 */
	public Flux<User> exchange() {
		TwitchRequest<UserList> request = route.newRequest();
		credential.ifPresent(cred -> {
			if (cred.scopes().contains(Scope.USER_READ_EMAIL)) {
				request.header("Authorization", "Bearer " + cred.accessToken());
			}
		});

		if (!userIds.isEmpty()) {
			userIds.forEach(id -> request.query("user_id", id.toString()));
		}
		if (!userLogins.isEmpty()) {
			userLogins.forEach(username -> request.query("user_login", username));
		}

		return request.exchange(router).flatMapMany(data -> Flux.fromIterable(data.getData()));
	}

	/**
	 * Update authorized user
	 * @param description Description your channel
	 * @return
	 */
	public Mono<User> updateUser(String description) {
		if (!credential.isPresent()) {
			return Mono.error(new CredentialNotFoundException("Required credentials to update your description."));
		} else {
			if (credential.get().scopes().contains(Scope.USER_EDIT)) {
				return Route.put("/users", UserList.class)
						.newRequest().query("description", description)
						.header("Authorization", "Bearer " + credential.get().accessToken())
						.exchange(router).flatMap(data -> Mono.just(data.getData().get(0)));
			} else {
				return Mono.error(new ScopeIsMissingException(Scope.USER_EDIT));
			}
		}
	}

}
