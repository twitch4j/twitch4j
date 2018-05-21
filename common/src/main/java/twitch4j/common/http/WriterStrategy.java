package twitch4j.common.http;

import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

public interface WriterStrategy<REQ> {
	boolean canWrite(Class<?> responseType, String contentType);
	RequestBody write(String contentType, REQ body) throws IOException;
}
