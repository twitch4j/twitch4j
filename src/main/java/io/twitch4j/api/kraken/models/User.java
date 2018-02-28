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

package io.twitch4j.api.kraken.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import io.twitch4j.api.Model;
import io.twitch4j.api.kraken.IKraken;
import io.twitch4j.auth.ICredential;
import io.twitch4j.auth.Scope;
import lombok.*;
import okhttp3.Response;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@ToString(exclude = {"credential"})
@EqualsAndHashCode(callSuper = false)
public class User extends Model {
	@JsonProperty("_id")
	private Long id;
	@JsonProperty("user")
	private String username;
	private String displayName;
	private String bio;
	private String logo;
	private String userType;
	@Getter(AccessLevel.NONE)
	private Calendar createdAt;
	@Getter(AccessLevel.NONE)
	private Calendar updatedAt;

	private Optional<ICredential> credential;

	public List<EmoteSet> getEmoteSets() {
		return null;
	}

	public Optional<Subscription> getChannelSubscription(Channel channel) {
		return getChannelSubscription(channel.getId());
	}

	public Optional<Subscription> getChannelSubscription(long channelId) {
		if (credential.isPresent()) {
			ICredential cred = credential.get();
			if (cred.getScopes().contains(Scope.USER_SUBSCRIPTIONS)) {
				try (Response response = getClient().getKrakenApi().get(String.format("/users/%d/subscriptions/%d", id, channelId), Collections.singletonMap("Authorization", cred.buildAuthorizationHeader(IKraken.PREFIX_AUTHORIZATION)))) {
					return Optional.of(getClient().getKrakenApi().buildPOJO(response, Subscription.class));
				} catch (IOException ignore) {
					// TODO: Exception Handling
				}
			}
		}

		return Optional.empty();
	}

	public PaginatedList<Follow> getFollows() {
		String url = String.format("/users/%d/follows/channels?limit=100", id);
		TypeReference<PaginatedList<Follow>> paginatedFollows = new TypeReference<PaginatedList<Follow>>(){};

		PaginatedList.InteractivePage<Follow> interactivePage = new PaginatedList.InteractivePage<Follow>() {
			@Override
			public PaginatedList<Follow> invoke(String cursor) {
				return null;
			}

			@Override
			public PaginatedList<Follow> invoke(int offset) {
				try (Response response = getClient().getKrakenApi().get(String.format("%s&offset=%d", url, offset))) {
					PaginatedList<Follow> follows = getClient().getKrakenApi().buildPOJO(response, paginatedFollows);

					follows.setOffset(offset);
					follows.setInteractivePage(this);

					return follows;
				} catch (IOException ignore) {
					// TODO: Exception Handling
				}
				return null;
			}
		};
		try (Response response = getClient().getKrakenApi().get(url)) {
			PaginatedList<Follow> follows = getClient().getKrakenApi().buildPOJO(response, paginatedFollows);
			follows.setOffset(0);
			follows.setInteractivePage(interactivePage);

			return follows;
		} catch (IOException ignore) {
			// TODO: Exception Handling
		}

		return null;
	}

	public Optional<Follow> getFollowInfo(Channel channel) {
		return getFollowInfo(channel.getId());
	}

	public Optional<Follow> getFollowInfo(long channelId) {
		try (Response response = getClient().getKrakenApi().get(String.format("/users/%d/follows/channels/%d", id, channelId))) {
			if (response.isSuccessful()) {
				return Optional.of(getClient().getKrakenApi().buildPOJO(response, Follow.class));
			} else return Optional.empty();
		} catch (IOException ignore) {
			// TODO: Exception Handling
		}
		return Optional.empty();
	}

	public Follow followChannel(Channel channel) {
		return followChannel(channel.getId());
	}

	public Follow followChannel(Channel channel, boolean push) {
		return followChannel(channel.getId(), push);
	}

	public Follow followChannel(long channelId) {
		return followChannel(channelId, false);
	}

	public Follow followChannel(long channelId, boolean push) {
		if (credential.isPresent()) {
			ICredential cred = credential.get();
			if (cred.getScopes().contains(Scope.USER_FOLLOWS_EDIT)) {
				try (Response response = getClient().getKrakenApi().put(String.format("/users/%d/subscriptions/%d%s", id, channelId, (push) ? "?notifications=true" : ""), null, Collections.singletonMap("Authorization", cred.buildAuthorizationHeader(IKraken.PREFIX_AUTHORIZATION)))) {
					return getClient().getKrakenApi().buildPOJO(response, Follow.class);
				} catch (IOException ignore) {
					// TODO: Exception Handling
				}
			}
		}
		return null;
	}

	public boolean unfollowChannel(Channel channel) {
		return unfollowChannel(channel.getId());
	}

	public boolean unfollowChannel(long channelId) {
		if (credential.isPresent()) {
			ICredential cred = credential.get();
			if (cred.getScopes().contains(Scope.USER_FOLLOWS_EDIT)) {
				try (Response response = getClient().getKrakenApi().delete(String.format("/users/%d/subscriptions/%d", id, channelId), Collections.singletonMap("Authorization", cred.buildAuthorizationHeader(IKraken.PREFIX_AUTHORIZATION)))) {
					return response.isSuccessful();
				} catch (IOException ignore) {
					// TODO: Exception Handling
				}
			}
		}
		return false;
	}

	public PaginatedList<Blocked> getBlockedUsers() {
		if (credential.isPresent()) {
			ICredential cred = credential.get();
			if (cred.getScopes().contains(Scope.USER_BLOCKS_READ)) {
				String url = String.format("/users/%d/follows/channels?limit=100", id);
				TypeReference<PaginatedList<Blocked>> paginatedType = new TypeReference<PaginatedList<Blocked>>(){};

				PaginatedList.InteractivePage<Blocked> interactivePage = new PaginatedList.InteractivePage<Blocked>() {
					@Override
					public PaginatedList<Blocked> invoke(String cursor) {
						return null;
					}

					@Override
					public PaginatedList<Blocked> invoke(int offset) {
						try (Response response = getClient().getKrakenApi().get(String.format("%s&offset=%d", url, offset), Collections.singletonMap("Authorization", cred.buildAuthorizationHeader(IKraken.PREFIX_AUTHORIZATION)))) {
							PaginatedList<Blocked> blocked = getClient().getKrakenApi().buildPOJO(response, paginatedType);

							blocked.setOffset(offset);
							blocked.setInteractivePage(this);

							return blocked;
						} catch (Exception ignore) {
							// TODO: Exception Handling
						}
						return null;
					}
				};

				try (Response response = getClient().getKrakenApi().get(url, Collections.singletonMap("Authorization", cred.buildAuthorizationHeader(IKraken.PREFIX_AUTHORIZATION)))) {
					PaginatedList<Blocked> blocked = getClient().getKrakenApi().buildPOJO(response, paginatedType);
					blocked.setOffset(0);
					blocked.setInteractivePage(interactivePage);

					return blocked;
				} catch (IOException ignore) {
					// TODO: Exception Handling
				}
			}
		}
		return null;
	}

	public Blocked blockUser(User user) {
		return blockUser(user.getId());
	}

	public Blocked blockUser(long userId) {
		if (credential.isPresent()) {
			ICredential cred = credential.get();
			if (cred.getScopes().contains(Scope.USER_BLOCKS_EDIT)) {
				try (Response response = getClient().getKrakenApi().put(String.format("/users/%d/blocks/%d", id, userId), null, Collections.singletonMap("Authorization", cred.buildAuthorizationHeader(IKraken.PREFIX_AUTHORIZATION)))) {
					return getClient().getKrakenApi().buildPOJO(response, Blocked.class);
				} catch (IOException ignore) {
					// TODO: Exception Handling
				}
			}
		}
		return null;
	}

	public boolean unblockUser(User user) {
		return unblockUser(user.getId());
	}

	public boolean unblockUser(long userId) {
		if (credential.isPresent()) {
			if (credential.get().getScopes().contains(Scope.USER_BLOCKS_EDIT)) {
				try (Response response = getClient().getKrakenApi().delete(String.format("/users/%d/blocks/%d", id, userId), Collections.singletonMap("Authorization", credential.get().buildAuthorizationHeader(IKraken.PREFIX_AUTHORIZATION)))) {
					return response.isSuccessful();
				} catch (IOException ignore) {
					// TODO: Exception Handling
				}
			}
		}
		return false;
	}
}
