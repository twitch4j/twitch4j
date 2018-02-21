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

import com.beust.jcommander.JCommander;
import io.twitch4j.utils.LoggerType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class Twitch4J {
	@NonNull private final Arguments arguments;
	// Jetty Server authorization.

	private void init() throws Exception {
		Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
			if (throwable != null) {
				LoggerFactory.getLogger(thread.getStackTrace()[0].getClassName())
						.error("Uncaught exception handler:", throwable);
			}
		});

		IClient client = Builder.newClient()
				.withClientId(arguments.getClientId())
				.withClientSecret(arguments.getClientSecret())
				.withBotCredential(
						Builder.newCredential()
								.withAccessToken(arguments.getBotToken())
								.withRefreshToken(arguments.getBotRefresh())
				)
				.withChannels(arguments.getChannels())
				.build();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				client.disconnect();
			} catch (Exception e) {
				LoggerFactory.getLogger(LoggerType.CORE)
						.error("Exception on shutdown Twitch Client", e);
				System.exit(1);
			}
		}));

		client.connect();
	}

	public static void main(String[] args) throws Exception {

		Arguments arguments = new Arguments();

		JCommander.newBuilder()
				.addObject(arguments)
				.args(args)
				.build();

		Twitch4J twitch = new Twitch4J(arguments);

		twitch.init();
	}
}
