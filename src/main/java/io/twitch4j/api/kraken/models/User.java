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
import io.twitch4j.api.Model;
import io.twitch4j.auth.ICredential;
import lombok.*;

import java.util.Calendar;
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
		return Optional.empty();
	}

	public List<Follow> getFollows() {
		return null;
	}

	public Optional<Follow> getFollowInfo(Channel channel) {
		return getFollowInfo(channel.getId());
	}

	public Optional<Follow> getFollowInfo(long channelId) {
		return Optional.empty();
	}

	public Follow followChannel(Channel channel) {
		return followChannel(channel.getId());
	}

	public Follow followChannel(long channelId) {
		return null;
	}

	public boolean unfollowChannel(Channel channel) {
		return unfollowChannel(channel.getId());
	}

	public boolean unfollowChannel(long channelId) {
		return false;
	}

	public List<Blocked> getBlockedUsers() {
		return null;
	}

	public Blocked blockUser(User user) {
		return blockUser(user.getId());
	}

	public Blocked blockUser(long userId) {
		return null;
	}

	public boolean unblockUser(User user) {
		return unblockUser(user.getId());
	}

	public boolean unblockUser(long userId) {
		return false;
	}
}
