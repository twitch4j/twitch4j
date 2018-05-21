package twitch4j.api.kraken.operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import twitch4j.api.TwitchKraken;
import twitch4j.api.kraken.endpoints.ChannelEndpoint;
import twitch4j.auth.ICredential;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChannelOperations extends AbstractKrakenOperation {
	private static final List<Duration> VALID_COMMERCIAL_LENGTHS = Stream.of(30L, 60L, 90L, 120L, 150L, 180L)
			.map(Duration::ofSeconds).collect(Collectors.toList());

	public ChannelOperations(OkHttpClient httpClient, ObjectMapper mapper) {
		super(httpClient, mapper);
	}


	public ChannelEndpoint getChannel(long id) {
		return new ChannelEndpoint(getHttpClient(), getMapper(), id);
	}

	public Optional<ChannelEndpoint> getChannel(String name) {
		return new TwitchKraken(getHttpClient(), getMapper())
				.userOperation()
				.getUser(name)
				.map(u -> getChannel(u.getId()));
	}

	public ChannelEndpoint getChannel(ICredential credential) {
		return new ChannelEndpoint(getHttpClient(), getMapper(), credential);
	}
}
