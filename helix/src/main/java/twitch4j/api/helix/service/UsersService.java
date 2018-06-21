package twitch4j.api.helix.service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twitch4j.api.helix.exceptions.ScopeIsMissingException;
import twitch4j.api.helix.model.Stream;
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

@Slf4j
public class UsersService extends AbstractService<UserList> {

	private final Optional<ICredential> credential;

	private final Set<Long> userIds = new LinkedHashSet<>();
	private final Set<String> usernames = new LinkedHashSet<>();

	public UsersService(Router router, Optional<ICredential> credential) {
		super(Route.get("/users", UserList.class), router);
		this.credential = credential;
	}

	public UsersService userId(Long userId) {
		if (userIds.size() == 100) {
			SizeLimitExceededException ex = new SizeLimitExceededException("Limit of user_id's is exceeded. Max 100.");
			log.error(ex.getMessage(), ex);
		} else  {
			this.userIds.add(userId);
		}
		return this;
	}
	public UsersService username(String username) {
		if (usernames.size() == 100) {
			SizeLimitExceededException ex = new SizeLimitExceededException("Limit of login's is exceeded. Max 100.");
			log.error(ex.getMessage(), ex);
		} else  {
			this.usernames.add(username);
		}
		return this;
	}

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
		if (!usernames.isEmpty()) {
			usernames.forEach(username -> request.query("user_login", username));
		}

		return request.exchange(router).flatMapMany(data -> Flux.fromIterable(data.getData()));
	}

	public Mono<User> updateUser(Stream description) {

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
