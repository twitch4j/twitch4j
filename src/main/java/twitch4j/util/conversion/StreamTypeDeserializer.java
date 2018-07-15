package twitch4j.util.conversion;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import twitch4j.enums.StreamType;

public class StreamTypeDeserializer extends JsonDeserializer<StreamType> {
	@Override
	public StreamType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return StreamType.of(p.getValueAsString());
	}
}
