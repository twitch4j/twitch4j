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

package io.twitch4j.runner;

import io.twitch4j.ITwitchClient;
import io.twitch4j.TwitchBuilder;
import io.twitch4j.IClient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Twitch4J implements Runnable {
	@CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "Display this help message and quits.")
	private boolean helpRequied = false;

	@CommandLine.Option(names = {"-C", "--client-id"}, usageHelp = true, description = "Twitch Client ID")
	private String clientId = System.getenv("TWITCH_CLIENT_ID");
	@CommandLine.Option(names = {"-S", "--client-secret"}, usageHelp = true, description = "Twitch Client Secret")
	private String clientSecret = System.getenv("TWITCH_CLIENT_SECRET");
	@CommandLine.Option(names = {"-B", "--bot-access-token"}, usageHelp = true, description = "Twitch Bot Access Token")
	private String botAccessToken = System.getenv("TWITCH_BOT_ACCESS_TOKEN");
	@CommandLine.Option(names = {"-R", "--bot-refresh-token"}, usageHelp = true, description = "Twitch Bot Refresh Token")
	private String botRefreshToken = System.getenv("TWITCH_BOT_REFRESH_TOKEN");

	public static void main(String[] args) {
		CommandLine.run(new Twitch4J(), System.out, args);
	}

	@Override
	public void run() {
		Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
			if (e != null) {
				Twitch4J.log.error("Uncaught exception handler:", e);
			}
		});

		try {
			ITwitchClient client = TwitchBuilder.newTwitchClient()
                    .withClientId(clientId)
                    .withClientSecret(clientSecret)
                    .withBotCredential(
                            TwitchBuilder.newCredential()
                            .withAccessToken(botAccessToken)
                            .withRefreshToken(botRefreshToken)
                    ).build();
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    client.disconnect();
                } catch (Exception e) {
                    Twitch4J.log.error("Exception on closing Twitch Client", e);
                    System.exit(1);
                }
            }));
			client.connect();
		} catch (Exception e) {
			Twitch4J.log.error("Exception on Twitch Client", e);
			System.exit(1);
		}
	}
}
