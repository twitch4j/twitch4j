package twitch4j.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Manager {
	private final AuthenticationService service;
	private final AuthorizationStorage storage;

	public void logIn() {
		service.authorize();
	}
}
