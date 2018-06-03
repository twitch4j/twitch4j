package twitch4j;

import com.beust.jcommander.JCommander;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.time.Duration;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.server.HttpServer;
import twitch4j.common.auth.ICredential;
import twitch4j.runner.Arguments;

public class Runner {
	public static void main(String[] args) {
		Arguments arguments = new Arguments();
		JCommander.newBuilder()
				.addObject(arguments)
				.build()
				.parse(args);
		if (arguments.isHelp()) {
			System.exit(0);
		}

		if (!System.getProperties().containsKey("twitch4j.log.level")) {
			System.setProperty("twitch4j.log.level", "INFO");
		}

		if (arguments.isDebug()) {
			System.setProperty("twitch4j.log.level", "DEBUG");
		}

		TwitchClient client = TwitchClient.builder()
				.clientId(arguments.getClientId())
				.clientSecret(arguments.getClientSecret())
				.botCredential(ICredential.builder()
						.accessToken(arguments.getBotAccessToken())
						.refreshToken(arguments.getBotRefreshToken())
				).connect();

		HttpServer.create(arguments.getPort()).startRouterAndAwait(
				routesBuilder -> routesBuilder
						.get("/**", (request, response) -> response.status(HttpResponseStatus.NOT_FOUND).send())
						.get("/login", (request, response) -> {
							QueryStringDecoder query = new QueryStringDecoder(request.uri());
							if (query.parameters().containsKey("error")) {
								response.status(HttpResponseStatus.FORBIDDEN)
										.sendString(Mono.just(query.parameters().get("error").get(0))).then();
							} else if (query.parameters().isEmpty()) {
								client.getCredentialManager().getService().createRedirectUri(query.uri(), null);
							}
							Mono<ICredential> credential = client.getCredentialManager().authorize(query.parameters().get("code").get(0));
							Mono<String> msg = credential.map(cred -> "Welcome " + cred.username());
							return response.sendString(msg).then(response.sendRedirect("https://twitch.tv/").take(Duration.ofSeconds(10)));
						}),
				blockingNettyContext -> {
					blockingNettyContext.installShutdownHook();
					blockingNettyContext.setLifecycleTimeout(Duration.ofDays(30));
				});
		Runtime.getRuntime().addShutdownHook(new Thread(() -> client.getMessageInterface().close()));
	}
}
