/*
 * MIT License
 *
 * Copyright (c) 2018 twitch4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.twitch4j;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.twitch4j.api.kraken.models.Channel;
import io.twitch4j.auth.ICredential;
import io.twitch4j.auth.ICredentialStorage;
import io.twitch4j.impl.Configuration;
import io.twitch4j.impl.TwitchClient;
import io.twitch4j.impl.auth.Credential;
import io.twitch4j.impl.auth.FileCredentialStorage;
import io.twitch4j.impl.pubsub.TwitchPubSubTopic;
import io.twitch4j.pubsub.ITopic;
import io.twitch4j.pubsub.Topic;
import lombok.*;
import lombok.experimental.Wither;
import org.apache.commons.lang3.Validate;

import java.util.*;
import java.util.function.Function;

/**
 * Builder that instantiates components of Twitch4J
 */
public class TwitchBuilder {

	/**
	 * Gets a builder to construct a new TwitchClient instance
	 *
	 * @return TwitchClientBuilder
	 */
	public static TwitchClientBuilder newTwitchClient() {
		return new TwitchClientBuilder();
	}

	public static Credentials newCredential() {
		return new Credentials();
	}

	public static PubSubTopic newTopic() { return new PubSubTopic(); }


	@Data
	@Wither
	@Setter(AccessLevel.NONE)
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class TwitchClientBuilder {
		/**
		 * The client id of your twitch app
		 */
		private String clientId;

		/**
		 * The client secret of your twitch app
		 */
		private String clientSecret;

		/**
		 * Bot Credentials
		 * @see Credential
		 */
		private Credentials botCredential;

		/**
		 * Credential Storage
		 */
		private Function<IClient, ? extends ICredentialStorage> credentialStorage = FileCredentialStorage::new;

		@Wither(AccessLevel.NONE)
		private Set<String> channels;

		@Wither(AccessLevel.NONE)
		private Set<ITopic> topics;

		public TwitchClientBuilder withTopics(ITopic... topics) {
			return withTopics(Arrays.asList(topics));
		}

		public TwitchClientBuilder withTopics(Collection<ITopic> topics) {
			this.topics = new LinkedHashSet<>(topics);
			return this;
		}

		public TwitchClientBuilder withChannels(String... channels) {
			return withChannels(Arrays.asList(channels));
		}

		public TwitchClientBuilder withChannels(Collection<String> channels) {
			if (this.channels != null) {
				this.channels.addAll(channels);
			}
			else {
				this.channels = new LinkedHashSet<>(channels);
			}
			return this;
		}

		public TwitchClientBuilder withChannel(String channel) {
			if (this.channels == null) {
				this.channels = new LinkedHashSet<>();
			}
			this.channels.add(channel);

			return this;
		}

		/**
		 * Build the Twitch Client instance
		 *
		 * @return IClient Twitch Client
		 */
		public ITwitchClient build() throws Exception {
			boolean pubSub = topics != null && !topics.isEmpty();

			Configuration configuration = new Configuration(
					Validate.notEmpty(this.clientId, "You need to provide a client id!"),
					Validate.notEmpty(this.clientSecret, "You need to provide a client secret!"),
					pubSub
			);
			TwitchClient client = new TwitchClient(configuration);

			client.getCredentialManager().setCredentialStorage(this.credentialStorage.apply(client));

			if (!Objects.isNull(this.botCredential)) {
				((Configuration) client.getConfiguration()).setBot((IConfiguration.IBot) client.getCredentialManager().buildCredentialData(this.botCredential));
			}

			client.getPubSub().registerTopics(topics);
			client.getMessageInterface().addChannels(channels);

			return client;
		}

		/**
		 * Build Twitch Client with connecting into Message Interface (IRC) and PubSub
		 * @return Twitch Client
		 * @throws Exception connection exception
		 */
		public IClient connect() throws Exception {
			TwitchClient client = (TwitchClient) build();
			client.connect();
			return client;
		}
	}

	@Data
	@Wither
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Credentials {
		private String accessToken;
		private String refreshToken;
		@Wither(AccessLevel.NONE)
		private DecodedJWT idToken;

		public Credentials withIdToken(String idToken) {
			return withIdToken(JWT.decode(idToken));
		}

		public Credentials withIdToken(DecodedJWT idToken) {
			this.idToken = idToken;
			return this;
		}
	}

	@Data
	@Wither
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class PubSubTopic {
		private Topic topic;
		private ICredential userCredential;
		private Channel channel;

		public ITopic buildTopic() {
			return new TwitchPubSubTopic(topic, channel, userCredential);
		}
	}
}
