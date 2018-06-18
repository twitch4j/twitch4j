package twitch4j.api;

import lombok.RequiredArgsConstructor;
import twitch4j.Configuration;
import twitch4j.api.helix.exceptions.ScopeIsMissingException;
import twitch4j.api.helix.service.BitsService;
import twitch4j.api.helix.service.ClipsCreationService;
import twitch4j.common.auth.ICredential;
import twitch4j.common.auth.Scope;
import twitch4j.stream.rest.request.Router;

@RequiredArgsConstructor
public class TwitchHelix {
	private final Router router;

	/**
	 * Required: {@link twitch4j.common.auth.Scope#BITS_READ}
	 * @return
	 */
	public BitsService getBitsLeaderboad(ICredential credential) {
		if (!credential.scopes().contains(Scope.BITS_READ)) {
			throw new ScopeIsMissingException(Scope.BITS_READ);
		}
		return new BitsService(router, credential);
	}

	public ClipsCreationService createClip(ICredential credential) {
		if (!credential.scopes().contains(Scope.CLIPS_EDIT)) {
			throw new ScopeIsMissingException(Scope.CLIPS_EDIT);
		}
		return new ClipsCreationService(router, credential);
	}
}
