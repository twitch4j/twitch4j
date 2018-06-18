package twitch4j.common.json.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import twitch4j.common.enums.BroadcasterType;

import java.io.IOException;

public class BroadcasterTypeDeserializer extends JsonDeserializer<BroadcasterType> {
	@Override
	public BroadcasterType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return BroadcasterType.of(p.getValueAsString());
	}
}
