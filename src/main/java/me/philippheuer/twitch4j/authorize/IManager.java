package me.philippheuer.twitch4j.authorize;

import me.philippheuer.twitch4j.api.model.ICredential;
import me.philippheuer.twitch4j.enums.Scope;
import org.springframework.social.connect.UsersConnectionRepository;

import java.util.Collection;

public interface IManager {
	UsersConnectionRepository getRepository();

	ICredential authorizeUser(Scope... scope);
	public ICredential authorizeUser(Collection<Scope> scope);
}
