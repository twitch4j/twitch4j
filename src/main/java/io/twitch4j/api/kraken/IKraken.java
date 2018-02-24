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

package io.twitch4j.api.kraken;

import io.twitch4j.api.IApi;
import io.twitch4j.api.kraken.models.IngestServer;
import io.twitch4j.api.kraken.models.Kraken;
import io.twitch4j.api.kraken.operations.*;
import io.twitch4j.auth.ICredential;
import io.twitch4j.utils.Unofficial;

import java.util.List;

public interface IKraken extends IApi {
	String PREFIX_AUTHORIZATION = "OAuth";

//	Bits bitsOperation();
	Feeds feedsOpration();
	Channels channelsOperation();
	Chat chatOperation();
	Clips clipsOpration();
	Collections collectionsOperation();
	Communities communitiesOperation();
	Games gamesOperation();
	List<IngestServer> getServerList() throws Exception;
	Streams streamsOperation();
	Teams teamsOperation();
	@Unofficial
	Undocumented undocumentedOperation();
	Users usersOperation();
	Videos videosOperation();
	Kraken fetchUserInfo(String accessToken) throws Exception;
}
