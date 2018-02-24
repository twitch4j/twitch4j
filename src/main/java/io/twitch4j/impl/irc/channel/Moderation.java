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

package io.twitch4j.impl.irc.channel;

import io.twitch4j.irc.channel.IEditor;
import io.twitch4j.irc.channel.IModeration;
import io.twitch4j.irc.exceptions.ModerationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import io.twitch4j.irc.channel.IEditor;
import io.twitch4j.irc.channel.IModeration;
import io.twitch4j.irc.exceptions.ModerationException;
import org.apache.commons.lang3.StringUtils;

@Getter
@RequiredArgsConstructor
public class Moderation implements IModeration {

	private final ChannelEndpoint endpoint;

	@Override
	public void banUser(String user, String reason) {
		endpoint.sendMessage(String.format("/ban %s%s", user, (StringUtils.isBlank(reason)) ? "" : " " + reason));
	}

	@Override
	public void timeoutUser(String user, int seconds, String reason) {
		endpoint.sendMessage(String.format("/timeout %s %d%s", user, seconds, (StringUtils.isBlank(reason)) ? "" : " " + reason));
	}

	@Override
	public void unbanUser(String user) {
		endpoint.sendMessage(String.format("/unband %s", user));
	}

	@Override
	public void emoteOnly() {
		endpoint.sendMessage("/emoteonly");
	}

	@Override
	public void emoteOnlyOff() {
		endpoint.sendMessage("/emoteonlyoff");
	}

	@Override
	public void r9k() {
		endpoint.sendMessage("/r9kbeta");
	}

	@Override
	public void r9kOff() {
		endpoint.sendMessage("/r9kbetaoff");
	}

	@Override
	public void slowmode(int seconds) {
		endpoint.sendMessage(String.format("/slow %d", seconds));
	}

	@Override
	public void slowmodeOff() {
		endpoint.sendMessage("/slowoff");
	}

	@Override
	public void clearChat() {
		endpoint.sendMessage("/clear");
	}

	@Override
	public void followersOnly(long seconds) {
		endpoint.sendMessage(String.format("/followers %d Seconds", seconds));
	}

	@Override
	public void followersOff() {
		endpoint.sendMessage("/followersoff");
	}

	@Override
	public IEditor getEditor() throws ModerationException {
		if (endpoint.isBotEditor()) {
			return new Editor(endpoint);
		} else throw new ModerationException("Bot is not editor on channel: " + endpoint.getChannel());
	}
}
