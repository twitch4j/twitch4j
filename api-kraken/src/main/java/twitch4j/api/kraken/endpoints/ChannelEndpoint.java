package twitch4j.api.kraken.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import twitch4j.common.auth.ICredential;

import java.util.Optional;

public class ChannelEndpoint extends AbstractEndpoint {
	private final long id;
	private final Optional<ICredential> credential;

	private ChannelEndpoint(OkHttpClient httpClient, ObjectMapper mapper, long id, ICredential credential) {
		super(httpClient, mapper);
		this.id = id;
		this.credential = Optional.ofNullable(credential);
	}

	public ChannelEndpoint(OkHttpClient httpClient, ObjectMapper mapper, long id) {
		this(httpClient, mapper, id, null);
	}

	public ChannelEndpoint(OkHttpClient httpClient, ObjectMapper mapper, ICredential credential) {
		this(httpClient, mapper, credential.user().getId(), credential);
	}


}
