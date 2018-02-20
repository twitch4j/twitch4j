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

package me.philippheuer.twitch4j;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import me.philippheuer.twitch4j.api.kraken.models.Channel;
import me.philippheuer.twitch4j.auth.ICredential;
import me.philippheuer.twitch4j.auth.ICredentialStorage;
import me.philippheuer.twitch4j.impl.Configuration;
import me.philippheuer.twitch4j.impl.TwitchClient;
import me.philippheuer.twitch4j.impl.auth.Credential;
import me.philippheuer.twitch4j.impl.auth.DefaultCredentialStorage;
import me.philippheuer.twitch4j.impl.pubsub.TwitchPubSubTopic;
import me.philippheuer.twitch4j.pubsub.ITopic;
import me.philippheuer.twitch4j.pubsub.Topic;
import org.apache.commons.lang3.Validate;

import java.util.*;
import java.util.function.Function;

public class Builder {

	public static Client newClient() {
		return new Client();
	}

	public static Credentials newCredential() {
		return new Credentials();
	}

	public static PubSubTopic newTopic() { return new PubSubTopic(); }

	@Data
	@Wither
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Client {
		/**
		 * [<b>REQUIRED</b>] Client ID
		 */
		private String clientId;
		/**
		 * [<b>REQUIRED</b>] Client Secret
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
		private Function<IClient, ? extends ICredentialStorage> credentialStorage = DefaultCredentialStorage::new;

		@Wither(AccessLevel.NONE)
		private Set<String> channels;

		@Wither(AccessLevel.NONE)
		private Set<ITopic> topics;

		public Client withTopics(ITopic... topics) {
			return withTopics(Arrays.asList(topics));
		}

		public Client withTopics(Collection<ITopic> topics) {
			this.topics = new LinkedHashSet<>(topics);
			return this;
		}

		public Client withChannels(String... channels) {
			return withChannels(Arrays.asList(channels));
		}

		public Client withChannels(Collection<String> channels) {
			if (this.channels != null) {
				this.channels.addAll(channels);
			}
			else {
				this.channels = new LinkedHashSet<>(channels);
			}
			return this;
		}

		public Client withChannel(String channel) {
			if (this.channels == null) {
				this.channels = new LinkedHashSet<>();
			}
			this.channels.add(channel);

			return this;
		}

		/**
		 * Build Twitch Client
		 * @return Twitch Client
		 */
		public IClient build() {
			boolean pubSub = topics != null && !topics.isEmpty();

			Configuration configuration = new Configuration(
					Validate.notEmpty(this.clientId, "You need to provide a client id!"),
					Validate.notEmpty(this.clientSecret, "You need to provide a client secret!"),
					pubSub
			);
			TwitchClient client = new TwitchClient(configuration);

			if (!Objects.isNull(this.botCredential)) {
				((Configuration) client.getConfiguration())
						.setBot((IConfiguration.IBot) this.botCredential.build(client, true));
			}

			client.getCredentialManager().setCredentialStorage(this.credentialStorage.apply(client));

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

		public ICredential build(IClient client) {
			return build(client, false);
		}

		public ICredential build(IClient client, boolean bot) {
			return client.getCredentialManager().buildCredentialData((bot) ? new TwitchClient.BotCredential(accessToken, refreshToken) : new Credential(accessToken, refreshToken, idToken));
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
