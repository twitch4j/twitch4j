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

package me.philippheuer;

import com.beust.jcommander.Parameter;
import lombok.Data;
import me.philippheuer.utils.Channels;

import java.util.Arrays;
import java.util.List;

@Data
class Arguments {
	@Parameter(names = {"-c", "--client-id"}, description = "Client ID")
	private String clientId = System.getenv("TWITCH_CLIENT_ID");
	@Parameter(names = {"-s", "--client-secret"}, description = "Client Secret")
	private String clientSecret = System.getenv("TWITCH_CLIENT_SECRET");
	@Parameter(names = {"-T", "--bot-token"}, description = "Bot Token")
	private String botToken = System.getenv("TWITCH_BOT_ACCESS_TOKEN");
	@Parameter(names = {"-R", "--bot-refresh"}, description = "Bot Refresh Token")
	private String botRefresh = System.getenv("TWITCH_BOT_REFRESH_TOKEN");
	@Parameter(names = {"-P", "--bot-prefix"}, description = "Bot Prefix Command")
	private String botPrefix = "!";
	@Parameter(names = {"-p", "--port"}, description = "Server Authorization Port")
	private int port = (System.getenv().containsKey("PORT")) ? Integer.parseInt(System.getenv("PORT")) : 8080;
	@Parameter(names = {"-C", "--channels" }, description = "Channel list", listConverter = Channels.class)
	private List<String> channels = Arrays.asList("jtv", "stachuofficialtv");
}
