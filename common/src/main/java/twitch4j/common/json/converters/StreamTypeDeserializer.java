package twitch4j.common.json.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import twitch4j.common.enums.StreamType;

import java.io.IOException;

public class StreamTypeDeserializer extends JsonDeserializer<StreamType> {
	@Override
	public StreamType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return StreamType.of(p.getValueAsString());
	}
}
