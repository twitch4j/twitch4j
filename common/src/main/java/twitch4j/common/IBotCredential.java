package twitch4j.common;

import twitch4j.common.auth.ICredential;

public interface IBotCredential extends ICredential {
	boolean isKnownBot();
	boolean isVerified();
}
