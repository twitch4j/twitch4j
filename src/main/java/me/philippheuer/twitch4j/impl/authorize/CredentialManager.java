package me.philippheuer.twitch4j.impl.authorize;

import lombok.Getter;
import me.philippheuer.twitch4j.IClient;
import me.philippheuer.twitch4j.api.model.ICredential;
import me.philippheuer.twitch4j.authorize.IManager;
import me.philippheuer.twitch4j.authorize.repositories.IRepository;
import me.philippheuer.twitch4j.enums.Scope;
import me.philippheuer.twitch4j.impl.TwitchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.security.provider.OAuth2AuthenticationService;

import java.util.Arrays;
import java.util.Collection;

@Getter
public class CredentialManager implements IManager {

	private final IClient client;

	private UsersConnectionRepository repository;

	public CredentialManager(IClient client) {
		this.client = client;
	}

	@Override
	public ICredential authorizeUser(Scope... scope) {
		return authorizeUser(Arrays.asList(scope));
	}

	@Override
	public ICredential authorizeUser(Collection<Scope> scope) {
		return null;
	}
}
