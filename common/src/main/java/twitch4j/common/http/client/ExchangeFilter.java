package twitch4j.common.http.client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import okhttp3.Request;
import okhttp3.Response;

import java.util.function.Consumer;

@Getter
@Builder(builderClassName = "Builder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeFilter {
	private final Consumer<Request.Builder> requestFilter;
	private final Consumer<Response> responseFilter;
}
