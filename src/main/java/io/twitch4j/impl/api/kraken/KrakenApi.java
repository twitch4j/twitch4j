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

package io.twitch4j.impl.api.kraken;

import io.twitch4j.ITwitchClient;
import io.twitch4j.enums.TwitchEndpoint;
import io.twitch4j.api.ApiException;
import io.twitch4j.api.kraken.IKraken;
import io.twitch4j.api.kraken.models.ErrorResponse;
import io.twitch4j.api.kraken.models.IngestServer;
import io.twitch4j.api.kraken.models.Kraken;
import io.twitch4j.api.kraken.operations.*;
import io.twitch4j.impl.api.Api;
import io.twitch4j.impl.api.kraken.operations.*;
import io.twitch4j.utils.Unofficial;
import okhttp3.Response;

import java.util.Arrays;
import java.util.List;

public class KrakenApi extends Api implements IKraken {

//	private final Bits bitsOperation;
	private final Feeds feedsOpration;
	private final Channels channelsOperation;
	private final Chat chatOperation;
	private final Clips clipsOpration;
	private final Collections collectionsOperation;
	private final Communities communitiesOperation;
	private final Games gamesOperation;
	private final Streams streamsOperation;
	private final Teams teamsOperation;
	@Unofficial
	private final Undocumented undocumentedOperation;
	private final Users usersOperation;
	private final Videos videosOperation;

	public KrakenApi(ITwitchClient client) {
		super(client, TwitchEndpoint.KRAKEN);

//		this.bitsOperation = new BitsOperation(this);
		this.feedsOpration = new FeedsOperation(this);
		this.channelsOperation = new ChannelsOperation(this);
		this.chatOperation = new ChatOperation(this);
		this.clipsOpration = new ClipsOperation(this);
		this.collectionsOperation = new CollectionsOperation(this);
		this.communitiesOperation = new CommunitiesOperation(this);
		this.gamesOperation = new GamesOperation(this);
		this.streamsOperation = new StreamsOperation(this);
		this.teamsOperation = new TeamsOperation(this);
		this.undocumentedOperation = new UndocumentedOperation(this);
		this.usersOperation = new UsersOperation(this);
		this.videosOperation = new VideosOperation(this);
	}

//	@Override
//	public Bits bitsOperation() {
//		return bitsOperation;
//	}

	@Override
	public Feeds feedsOpration() {
		return feedsOpration;
	}

	@Override
	public Channels channelsOperation() {
		return channelsOperation;
	}

	@Override
	public Chat chatOperation() {
		return chatOperation;
	}

	@Override
	public Clips clipsOpration() {
		return clipsOpration;
	}

	@Override
	public Collections collectionsOperation() {
		return collectionsOperation;
	}

	@Override
	public Communities communitiesOperation() {
		return communitiesOperation;
	}

	@Override
	public Games gamesOperation() {
		return gamesOperation;
	}


	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List<IngestServer> getServerList() throws Exception {
		String url = String.format("%s/ingests", TwitchEndpoint.KRAKEN.getUrl().substring(0, TwitchEndpoint.KRAKEN.getUrl().length()-1));
		try (Response response = get(url)) {
			if (response.isSuccessful()) {
				return Arrays.asList(buildPOJO(response, IngestServer[].class));
			} else throw handleException(response);
		}
	}

	@Override
	public Streams streamsOperation() {
		return streamsOperation;
	}

	@Override
	public Teams teamsOperation() {
		return teamsOperation;
	}

	@Override
	public @Unofficial Undocumented undocumentedOperation() {
		return undocumentedOperation;
	}

	@Override
	public Users usersOperation() {
		return usersOperation;
	}

	@Override
	public Videos videosOperation() {
		return videosOperation;
	}

	public Kraken fetchUserInfo(String accessToken) throws Exception {
		try (Response response = get(TwitchEndpoint.KRAKEN.getUrl(), java.util.Collections.singletonMap("Authorization", buildAuthorizationHeader(accessToken)))) {
			if (response.isSuccessful()) {
				return buildPOJO(response, Kraken.class);
			} else throw handleException(response);
		}
	}

	private ApiException handleException(Response response) throws Exception {
		return new ApiException(buildPOJO(response, ErrorResponse.class));
	}

	private String buildAuthorizationHeader(String accessToken) {
		return String.format("%s %s", IKraken.PREFIX_AUTHORIZATION, accessToken);
	}
}
