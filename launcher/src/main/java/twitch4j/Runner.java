package twitch4j;

import io.netty.handler.codec.http.QueryStringDecoder;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.server.HttpServer;

import java.time.Duration;

public class Runner {
	public static void main(String[] args) {

		HttpServer server = HttpServer.create(8080);
		server.startRouterAndAwait(
				routesBuilder -> {
					routesBuilder.get("/**", (request, response) -> {
						QueryStringDecoder query = new QueryStringDecoder(request.uri());
						return response.sendString(Mono.just(query.parameters().toString())).then();
					});
				},
				blockingNettyContext -> {
					blockingNettyContext.installShutdownHook();
					blockingNettyContext.setLifecycleTimeout(Duration.ofDays(14));
				});


	}
}
