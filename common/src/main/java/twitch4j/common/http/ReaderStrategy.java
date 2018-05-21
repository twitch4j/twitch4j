package twitch4j.common.http;

import okhttp3.Response;

import java.io.IOException;

public interface ReaderStrategy<RES> {
	boolean canRead(Class<?> responseType, String header);
	RES read(Response response, Class<RES> responseType) throws IOException;
}
