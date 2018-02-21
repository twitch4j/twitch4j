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
import lombok.*;
import io.twitch4j.api.IApi;
import io.twitch4j.api.Model;
import io.twitch4j.api.OnField;
import okhttp3.Response;
import retrofit2.http.*;

import java.util.Calendar;
import java.util.Optional;

@Data
@AllArgsConstructor
@ToString(exclude = {"endpoint"})
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

	private final UserEndpoints endpoint = ((IApi) getClient().getKrakenApi()).createService(UserEndpoints.class);

	private interface UserEndpoints {
		@GET("/users/{id}/emotes")
		EmoteSet getEmoteSet(@Header("Authorization") String token, @Path("id") long userId);

		@GET("/users/{id}/subscriptions/{channel_id}")
		Optional<Subscription> getChannelSubscription(@Header("Authorization") String token, @Path("id") long userId, @Path("channel_id") long channelId);

		@OnField("follows")
		@GET("/users/{id}/follows/channels")
		PaginatedList<Follow> getFollows(@Path("id") long userId);

		@GET("/users/{user_id}/follows/channels/{channel_id}")
		Optional<Follow> getFollow(@Path("user_id") long userId, @Path("channel_id") long channelId);

		@PUT("/users/{id}/follows/channels/{channel_id}")
		Follow followChannel(@Header("Authorization") String token, @Path("id") long userId, @Path("channel_id") long channelId, @Query("notifications") boolean notifications);

		@DELETE("/users/{id}/follows/channels/{channel_id}")
		Response unfollowChannel(@Header("Authorization") String token, @Path("id") long userId, @Path("channel_id") long channelId);

		@OnField("blocks")
		@GET("/users/{id}/blocks")
		PaginatedList<Blocked> getBlockedUser(@Header("Authorization") String token, @Path("id") long userId);

		@PUT("/users/{credential.user.id}/blocks/{user_id}")
		Blocked blockUser(@Header("Authorization") String token, @Path("id") long userId, @Path("blocked_id") long blockedUserId);

		@DELETE("/users/{id}/blocks/{blocked_id}")
		Response unblockUser(@Header("Authorization") String token, @Path("id") long userId, @Path("blocked_id") long blockedUserId);
	}
}
